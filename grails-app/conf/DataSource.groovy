dataSource {
    pooled = true
    jmxExport = true
    //driverClassName = "org.h2.Driver" //h2
    //driverClassName = "org.postgresql.Driver" //postgresql
    //dialect = "org.hibernate.dialect.PostgreSQLDialect" //postgresql
    driverClassName = "com.mysql.jdbc.Driver" //mysql
    dialect = "org.hibernate.dialect.MySQL5InnoDBDialect" //mysql
    username = "reverie_user" //mysql
    password = "graduation" //mysql

}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            //url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE" //h2
            //url = "jdbc:postgresql://localhost:5432/reverie_db" //postgresql
            //url = "jdbc:postgres://kbymizohdshdgj:sDGsOUPJY5DCdawtMSZ-nmXAU-@ec2-50-19-233-111.compute-1.amazonaws.com:5432/dcq98mda5r95fb" //postgres-heroku
            url = "jdbc:mysql://localhost/reverie_db?useUnicode=yes&characterEncoding=UTF-8" //mysql
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            //url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE" //h2
            //url = "jdbc:postgresql://localhost:5432/reverie_db" //postgresql
            //url = "jdbc:postgres://kbymizohdshdgj:sDGsOUPJY5DCdawtMSZ-nmXAU-@ec2-50-19-233-111.compute-1.amazonaws.com:5432/dcq98mda5r95fb" //postgres-heroku
            url = "jdbc:mysql://localhost/reverie_db?useUnicode=yes&characterEncoding=UTF-8" //mysql
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            //url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE" //h2
            //url = "jdbc:postgresql://localhost:5432/reverie_db" //postgresql
            url = "jdbc:mysql://localhost/reverie_db?useUnicode=yes&characterEncoding=UTF-8" //mysql
            //url = "jdbc:postgres://kbymizohdshdgj:sDGsOUPJY5DCdawtMSZ-nmXAU-@ec2-50-19-233-111.compute-1.amazonaws.com:5432/dcq98mda5r95fb" //postgres-heroku
            properties {
               // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
               jmxEnabled = true
               initialSize = 5
               maxActive = 50
               minIdle = 5
               maxIdle = 25
               maxWait = 10000
               maxAge = 10 * 60000
               timeBetweenEvictionRunsMillis = 5000
               minEvictableIdleTimeMillis = 60000
               validationQuery = "SELECT 1"
               validationQueryTimeout = 3
               validationInterval = 15000
               testOnBorrow = true
               testWhileIdle = true
               testOnReturn = false
               jdbcInterceptors = "ConnectionState"
               defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
            }
        }
    }
}
