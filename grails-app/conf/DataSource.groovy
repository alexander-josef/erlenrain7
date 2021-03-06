dataSource {
  pooled = true
  driverClassName = "org.hsqldb.jdbcDriver"
  username = "sa"
  password = ""
}
hibernate {
  cache.use_second_level_cache = true
  cache.use_query_cache = true
  cache.provider_class = 'com.opensymphony.oscache.hibernate.OSCacheProvider'
}
// environment specific settings
environments {
  development {
    dataSource {
      dbCreate = "create-drop" // one of 'create', 'create-drop','update'
      url = "jdbc:hsqldb:mem:devDB"
      loggingSql = false
    }
  }
  test {
    dataSource {
      pooled = true
      driverClassName = "org.postgresql.Driver"
      username = "unartig"
      password = "unartig"

      dbCreate = "update"
      url = "jdbc:postgresql://localhost:5432/erlenrain7"
    }
  }
  production {
    dataSource {
      pooled = true
      driverClassName = "org.postgresql.Driver"
      username = "unartig"
      password = "unartig"

      dbCreate = "update"
      url = "jdbc:postgresql://localhost:5432/erlenrain7"
    }
  }
}