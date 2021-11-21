package com.zero.treasure.postgresql;

public class QueryMetadata {

    public void tables(String schema){
        String sql = "SELECT t.tablename AS NAME, c.COMMENT AS table_remark, 'TABLE' AS type FROM pg_tables AS t " +
                "INNER JOIN (SELECT relname, cast( obj_description ( relfilenode, 'pg_class' ) AS VARCHAR ) AS COMMENT " +
                "FROM pg_class c WHERE relname IN ( SELECT tablename FROM pg_tables WHERE schemaname = '" + schema  + "') " +
                "and c.relnamespace in (select oid from pg_namespace  where nspname = '" + schema  + "')) " +
                "AS c ON t.tablename = c.relname WHERE t.schemaname = '" + schema  + "'";
    }

    public void columns(String schema, String table){
        String sql = "select sc.column_name as name, sc.udt_name as type ,sd.descript as remark from information_schema.columns as sc\n" +
                "left join (select a.attnum,(select description from pg_catalog.pg_description where objoid=a.attrelid and objsubid=a.attnum) " +
                "as descript ,a.attname,pg_catalog.format_type(a.atttypid,a.atttypmod) as data_type from pg_catalog.pg_attribute a where " +
                "a.attrelid=(select oid from pg_class where relname='" + table + "' and relnamespace in (select oid from pg_namespace where " +
                "nspname = '" + schema + "')) and a.attnum>0 and not a.attisdropped order by a.attnum) as sd\n" +
                "on sc.column_name = attname where sc.table_schema = '" + schema + "' and sc.table_name = '" + table + "'";
    }

}
