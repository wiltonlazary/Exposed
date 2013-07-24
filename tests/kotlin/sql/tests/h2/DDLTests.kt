package kotlin.sql.tests.h2

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.sql.*

public class DDLTests {
    var db = Database("jdbc:h2:mem:test", driver = "org.h2.Driver")

    Test fun unnamedTableWithQuotesSQL() {
        object TestTable : Table() {
            val id = integer("id", ColumnType.PRIMARY_KEY)
            val name = varchar("name", length = 42)
        }

        db.withSession {
            assertEquals("CREATE TABLE \"unnamedTableWithQuotesSQL\$TestTable\" (id INT PRIMARY KEY NOT NULL, name VARCHAR(42) NOT NULL)", TestTable.ddl)
        }
    }

    Test fun namedEmptyTableWithoutQuotesSQL() {
        object TestTable : Table("test_named_table") {
        }

        db.withSession {
            assertEquals("CREATE TABLE test_named_table", TestTable.ddl)
        }
    }

    Test fun tableWithDifferentColumnTypesSQL() {
        object TestTable : Table("test_table_with_different_column_types") {
            val id = integer("id", autoIncrement = true)
            val name = varchar("name", ColumnType.PRIMARY_KEY, length = 42)
            val age = integer("age", ColumnType.NULLABLE)
        }

        db.withSession {
            assertEquals("CREATE TABLE test_table_with_different_column_types (id INT AUTO_INCREMENT NOT NULL, name VARCHAR(42) PRIMARY KEY NOT NULL, age INT NULL)", TestTable.ddl)
        }
    }

    Test fun tablesWithCrossReferencesSQL() {
        /*db.withSession {
            assertEquals("CREATE TABLE test_table_with_different_column_types (id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, name VARCHAR(42) NOT NULL, age INT NULL)", TestTable.ddl)
        }*/
    }
}

object TestTableWithReference1 : Table("test_table_1") {
    val id = integer("id", ColumnType.PRIMARY_KEY)
    val testTable2Id = integer("id", references = TestTableWithReference2.id)
}

object TestTableWithReference2 : Table("test_table_1") {
    val id = integer("id", ColumnType.PRIMARY_KEY)
    val testTable1Id = integer("id", ColumnType.NULLABLE, references = TestTableWithReference1.id)
}
