package SQLDumper

import java.sql.{ResultSet, Connection}

trait QueryFactory {
  def apply(connectionFactory: ConnectionFactory, queryString: String, params: Any*): Query
}

trait Query {
  def select[A](f: ResultSet => A): Seq[A]
  def execute(): Int
  def cancel()
}
