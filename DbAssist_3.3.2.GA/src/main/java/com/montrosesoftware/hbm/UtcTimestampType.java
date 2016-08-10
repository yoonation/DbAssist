package com.montrosesoftware.hbm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.hibernate.type.TimestampType;

public class UtcTimestampType extends TimestampType {

    private static final long serialVersionUID = 8077663283676934687L;
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    @Override
    public Object get(ResultSet rs, String name) throws SQLException {
        return rs.getTimestamp(name, Calendar.getInstance(UTC));
    }

    @Override
    public void set(PreparedStatement st, Object value, int index) throws SQLException {
        Timestamp ts;
        if(value instanceof Timestamp) {
            ts = (Timestamp) value;
        } else {
            ts = new Timestamp(((Date) value).getTime());
        }
        st.setTimestamp(index, ts, Calendar.getInstance(UTC));
    }
}