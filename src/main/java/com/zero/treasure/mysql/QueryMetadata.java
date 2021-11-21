package com.zero.treasure.mysql;

public class QueryMetadata {

    public void tables(String schema) {
        String sql = "SELECT TABLE_NAME as name, TABLE_COMMENT as table_remark, 'TABLE' as type FROM information_schema.TABLES " +
                "WHERE TABLE_TYPE='base table' AND TABLE_SCHEMA = '" + schema  + "'";
    }

    public void columns(String schema, String table) {
        String sql = "SELECT COLUMN_NAME as name, DATA_TYPE as type, COLUMN_COMMENT as remark " +
                "FROM information_schema.COLUMNS WHERE table_name = '" + table + "' AND TABLE_SCHEMA = '" + schema + "'";
    }

}
