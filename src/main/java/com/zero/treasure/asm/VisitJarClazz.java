package com.zero.treasure.asm;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class VisitJarClazz {


    /**
     * 读取jar包里的class文件并查询包含main方法的
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String filePath = "./lib/treasure-0.0.1-SNAPSHOT.jar";
        System.out.println(loadMainClazzName(filePath));
    }

    private static List<String> loadMainClazzName(String filePath) throws IOException {
        ZipFile zipFile = new ZipFile(filePath);
        List<ZipArchiveEntry> entries = loadJarClazz(zipFile);
        return entries.stream().map(entry -> {
            InputStream inputStream = null;
            try {
                inputStream = zipFile.getInputStream(entry);
                ClassReader classReader = new ClassReader(inputStream);
                MainClassVisitor mainClassVisitor = new MainClassVisitor(entry.getName(), Opcodes.ASM7);
                classReader.accept(mainClassVisitor, ClassReader.SKIP_DEBUG);
                return mainClassVisitor;
            } catch (Throwable e) {
                log.error("", e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
            return null;
        }).filter(Objects::nonNull).filter(MainClassVisitor::isMain).map(MainClassVisitor::getPath).collect(Collectors.toList());
    }

    private static List<ZipArchiveEntry> loadJarClazz(ZipFile zipFile) throws IOException {
        List<ZipArchiveEntry> result = new LinkedList<>();
        Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
        while (entries.hasMoreElements()) {
            ZipArchiveEntry entry = entries.nextElement();
            if(entry.isDirectory()){
                continue;
            }
            if(entry.getName().endsWith(".class")){
                result.add(entry);
            }
        }
        return result;
    }

    @Getter
    static class MainClassVisitor extends ClassVisitor {

        private final String path;

        private boolean isMain;

        public MainClassVisitor(String path, int api) {
            super(api);
            this.path = path;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            // 即将抛弃的方法也会显示
            if(((Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC) == access || (Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_DEPRECATED) == access)
                    && "main".equals(name)
                    && "([Ljava/lang/String;)V".equals(descriptor)){
                this.isMain = true;
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

    }

}
