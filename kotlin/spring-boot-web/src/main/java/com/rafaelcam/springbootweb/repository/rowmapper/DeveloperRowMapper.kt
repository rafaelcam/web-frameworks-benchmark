package com.rafaelcam.springbootweb.repository.rowmapper

import com.rafaelcam.springbootweb.model.Developer
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.*

class DeveloperRowMapper : RowMapper<Developer> {

    override fun mapRow(rs: ResultSet, i: Int): Developer {
        return Developer(
                id = UUID.fromString(rs.getString("id")),
                name = rs.getString("name"),
                age = rs.getInt("age"),
                expertise = rs.getString("expertise"),
                createdAt = rs.getTimestamp("created_at").toInstant()
        )
    }
}