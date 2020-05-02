package com.rafaelcam.springbootweb.repository.query

object DeveloperSQLExpressions {

    const val INSERT = """
        INSERT INTO developer(
            id, 
            name, 
            age, 
            expertise,
            created_at) 
        VALUES (
            :id,
            :name,
            :age,
            :expertise,
            :created_at)
    """

    const val SELECT_ALL = """
        SELECT * FROM developer
    """

    const val SELECT_BY_ID = """
        SELECT * FROM developer WHERE id = :id
    """

    const val DELETE_BY_ID = """
        DELETE FROM developer WHERE id = :id
    """
}