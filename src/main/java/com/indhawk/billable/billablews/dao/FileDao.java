package com.indhawk.billable.billablews.dao;

import com.indhawk.billable.models.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Slf4j
public class FileDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public  boolean saveFile(File file) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("file_name", file.getFileName())
                .addValue("file_type", file.getFileType())
                .addValue("file_bytes", file.getBytes())
                .addValue("org_id", file.getOrgId());
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SaveFile");
        call.execute(params);
        return true;
    }

    public File getFileByOrgId(int orgId) {
        String function = "SELECT * FROM GetFileByOrgId(?)";
        return jdbcTemplate.queryForObject(
                function,
                new FileDao.FileRowMapper(),
                new Object[]{orgId});
    }

    private class FileRowMapper implements RowMapper<File> {

        @Override
        public File mapRow(ResultSet rs, int rowNum) throws SQLException {
            File file = File.builder()
                    .fileId(rs.getInt("fileid"))
                    .fileName(rs.getString("name"))
                    .fileType(rs.getString("type"))
                    .bytes(rs.getBytes("bytes"))
                    .orgId(rs.getInt("orgid"))
                    .build();
            return file;
        }
    }
}
