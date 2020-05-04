package com.rafaelcam.vertx.repository.query

object DeveloperSQLExpressions {

    const val INSERT = """
        INSERT INTO developer(id, name, age, expertise, created_at)
        VALUES ($1, $2, $3, $4, $5)
    """

    const val SELECT_ALL = """
        SELECT id, name, age, expertise, created_at FROM developer
    """

    const val SELECT_BY_ID = """
        SELECT id, name, age, expertise, created_at FROM developer WHERE id = $1
    """

    const val DELETE_BY_ID = """
        DELETE FROM developer WHERE id = $1
    """
}