/*
 * mysqllib
 * 
 * Created on 26 November 2014 at 4:27 PM.
 */

package com.maulss.enhancedlib.database.mysql;

import com.maulss.enhancedlib.database.Credentials;

/**
 * @author Maulss
 */
public final class MySQLProperties {

	private enum Defaults {

		LOGGER("com.mysql.jdbc.log.StandardLogger"),
		ALLOW_MULTI_QUERIES(false),
		AUTO_RECONNECT(false),
		AUTO_RECONNECT_FOR_POOLS(false),
		CACHE_CALLABLE_STMTS(false),
		CACHE_PREP_STMTS(false),
		CREATE_DATABASE_IF_NOT_EXIST(false),
		DONT_CHECK_ON_DUPLICATE_KEY_UPDATE_IN_SQL(false),
		EMPTY_STRINGS_CONVERT_TO_ZERO(true),
		ENABLE_QUERY_TIMEOUTS(true),
		FAIL_OVER_READ_ONLY(true),
		HOLD_RESULTS_OPEN_OVER_STATEMENT_CLOSE(false),
		MAINTAIN_TIME_STATS(true),
		PARANOID(false),
		PROFILE_SQL(false),
		REQUIRE_SSL(false),
		TCP_KEEP_ALIVE(true),
		TCP_NO_DELAY(true),
		USE_CURSOR_FETCH(false),
		USE_SSL(false),
		CALLABLE_STMT_CACHE_SIZE(200),
		CONNECT_TIMEOUT(0),
		INITIAL_TIMEOUT(2),
		LARGE_ROW_SIZE_THRESHOLD(2048),
		MAX_RECONNECTS(3),
		MAX_ROWS(-1),
		METADATA_CACHE_SIZE(50),
		PREP_STMT_CACHE_SIZE(25),
		PREP_STMT_CACHE_SQL_LIMIT(256);

		private String  strVal;
		private boolean booVal;
		private int     intVal;

		Defaults(String strVal)  { this.strVal = strVal; }
		Defaults(boolean booVal) { this.booVal = booVal; }
		Defaults(int intVal)     { this.intVal = intVal; }

		private String  strVal() { return strVal; }
		private boolean booVal() { return booVal; }
		private int intVal()     { return intVal; }
	}

	private Credentials credentials;
	private String      logger                             = "com.mysql.jdbc.log.StandardLogger";
	private boolean     allowMultiQueries                  = false;
	private boolean     autoReconnect                      = false;
	private boolean     autoReconnectForPools              = false;
	private boolean     cacheCallableStmts                 = false;
	private boolean     cachePrepStmts                     = false;
	private boolean     createDatabaseIfNotExist           = false;
	private boolean     dontCheckOnDuplicateKeyUpdateInSQL = false;
	private boolean     emptyStringsConvertToZero          = true;
	private boolean     enableQueryTimeouts                = true;
	private boolean     failOverReadOnly                   = true;
	private boolean     holdResultsOpenOverStatementClose  = false;
	private boolean     maintainTimeStats                  = true;
	private boolean     paranoid                           = false;
	private boolean     profileSQL                         = false;
	private boolean     requireSSL                         = false;
	private boolean     tcpKeepAlive                       = true;
	private boolean     tcpNoDelay                         = true;
	private boolean     useCursorFetch                     = false;
	private boolean     useSSL                             = false;
	private int         callableStmtCacheSize              = 200;
	private int         connectTimeout                     = 0;
	private int         initialTimeout                     = 2;
	private int         largeRowSizeThreshold              = 2048;
	private int         maxReconnects                      = 3;
	private int         maxRows                            = -1;
	private int         metadataCacheSize                  = 50;
	private int         prepStmtCacheSize                  = 25;
	private int         prepStmtCacheSqlLimit              = 256;

	public Credentials getCredentials() {
		return credentials;
	}

	public void credentials                        (Credentials credentials)                    { this.credentials = credentials;                                               }
	public void logger                             (String logger)                              { this.logger = logger;                                                         }
	public void allowMultiQueries                  (boolean allowMultiQueries)                  { this.allowMultiQueries = allowMultiQueries;                                   }
	public void autoReconnect                      (boolean autoReconnect)                      { this.autoReconnect = autoReconnect;                                           }
	public void autoReconnectForPools              (boolean autoReconnectForPools)              { this.autoReconnectForPools = autoReconnectForPools;                           }
	public void cacheCallableStmts                 (boolean cacheCallableStmts)                 { this.cacheCallableStmts = cacheCallableStmts;                                 }
	public void cachePrepStmts                     (boolean cachePrepStmts)                     { this.cachePrepStmts = cachePrepStmts;                                         }
	public void createDatabaseIfNotExist           (boolean createDatabaseIfNotExist)           { this.createDatabaseIfNotExist = createDatabaseIfNotExist;                     }
	public void dontCheckOnDuplicateKeyUpdateInSQL (boolean dontCheckOnDuplicateKeyUpdateInSQL) { this.dontCheckOnDuplicateKeyUpdateInSQL = dontCheckOnDuplicateKeyUpdateInSQL; }
	public void emptyStringsConvertToZero          (boolean emptyStringsConvertToZero)          { this.emptyStringsConvertToZero = emptyStringsConvertToZero;                   }
	public void enableQueryTimeouts                (boolean enableQueryTimeouts)                { this.enableQueryTimeouts = enableQueryTimeouts;                               }
	public void failOverReadOnly                   (boolean failOverReadOnly)                   { this.failOverReadOnly = failOverReadOnly;                                     }
	public void holdResultsOpenOverStatementClose  (boolean holdResultsOpenOverStatementClose)  { this.holdResultsOpenOverStatementClose = holdResultsOpenOverStatementClose;   }
	public void maintainTimeStats                  (boolean maintainTimeStats)                  { this.maintainTimeStats = maintainTimeStats;                                   }
	public void paranoid                           (boolean paranoid)                           { this.paranoid = paranoid;                                                     }
	public void profileSQL                         (boolean profileSQL)                         { this.profileSQL = profileSQL;                                                 }
	public void requireSSL                         (boolean requireSSL)                         { this.requireSSL = requireSSL;                                                 }
	public void tcpKeepAlive                       (boolean tcpKeepAlive)                       { this.tcpKeepAlive = tcpKeepAlive;                                             }
	public void tcpNoDelay                         (boolean tcpNoDelay)                         { this.tcpNoDelay = tcpNoDelay;                                                 }
	public void useCursorFetch                     (boolean useCursorFetch)                     { this.useCursorFetch = useCursorFetch;                                         }
	public void useSSL                             (boolean useSSL)                             { this.useSSL = useSSL;                                                         }
	public void callableStmtCacheSize              (int callableStmtCacheSize)                  { this.callableStmtCacheSize = callableStmtCacheSize;                           }
	public void connectTimeout                     (int connectTimeout)                         { this.connectTimeout = connectTimeout;                                         }
	public void initialTimeout                     (int initialTimeout)                         { this.initialTimeout = initialTimeout;                                         }
	public void largeRowSizeThreshold              (int largeRowSizeThreshold)                  { this.largeRowSizeThreshold = largeRowSizeThreshold;                           }
	public void maxReconnects                      (int maxReconnects)                          { this.maxReconnects = maxReconnects;                                           }
	public void maxRows                            (int maxRows)                                { this.maxRows = maxRows;                                                       }
	public void metadataCacheSize                  (int metadataCacheSize)                      { this.metadataCacheSize = metadataCacheSize;                                   }
	public void prepStmtCacheSize                  (int prepStmtCacheSize)                      { this.prepStmtCacheSize = prepStmtCacheSize;                                   }
	public void prepStmtCacheSqlLimit              (int prepStmtCacheSqlLimit)                  { this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;                           }

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();

		if (!logger.equals(Defaults.LOGGER.strVal())) s.append(logger);
		if (allowMultiQueries                  != Defaults.ALLOW_MULTI_QUERIES.booVal())                       s.append("&").append("allowMultiQueries=").append(allowMultiQueries);
		if (autoReconnect                      != Defaults.AUTO_RECONNECT.booVal())                            s.append("&").append("autoReconnect=").append(autoReconnect);
		if (autoReconnectForPools              != Defaults.AUTO_RECONNECT_FOR_POOLS.booVal())                  s.append("&").append("autoReconnectForPools=").append(autoReconnectForPools);
		if (cacheCallableStmts                 != Defaults.CACHE_CALLABLE_STMTS.booVal())                      s.append("&").append("cacheCallableStmts=").append(cacheCallableStmts);
		if (cachePrepStmts                     != Defaults.CACHE_PREP_STMTS.booVal())                          s.append("&").append("cachePrepStmts=").append(cachePrepStmts);
		if (createDatabaseIfNotExist           != Defaults.CREATE_DATABASE_IF_NOT_EXIST.booVal())              s.append("&").append("createDatabaseIfNotExist=").append(createDatabaseIfNotExist);
		if (dontCheckOnDuplicateKeyUpdateInSQL != Defaults.DONT_CHECK_ON_DUPLICATE_KEY_UPDATE_IN_SQL.booVal()) s.append("&").append("dontCheckOnDuplicateKeyUpdateInSQL=").append(dontCheckOnDuplicateKeyUpdateInSQL);
		if (emptyStringsConvertToZero          != Defaults.EMPTY_STRINGS_CONVERT_TO_ZERO.booVal())             s.append("&").append("emptyStringsConvertToZero=").append(emptyStringsConvertToZero);
		if (enableQueryTimeouts                != Defaults.ENABLE_QUERY_TIMEOUTS.booVal())                     s.append("&").append("enableQueryTimeouts=").append(enableQueryTimeouts);
		if (failOverReadOnly                   != Defaults.FAIL_OVER_READ_ONLY.booVal())                       s.append("&").append("failOverReadOnly=").append(failOverReadOnly);
		if (holdResultsOpenOverStatementClose  != Defaults.HOLD_RESULTS_OPEN_OVER_STATEMENT_CLOSE.booVal())    s.append("&").append("holdResultsOpenOverStatementClose=").append(holdResultsOpenOverStatementClose);
		if (maintainTimeStats                  != Defaults.MAINTAIN_TIME_STATS.booVal())                       s.append("&").append("maintainTimeStats=").append(maintainTimeStats);
		if (paranoid                           != Defaults.PARANOID.booVal())                                  s.append("&").append("paranoid=").append(paranoid);
		if (profileSQL                         != Defaults.PROFILE_SQL.booVal())                               s.append("&").append("profileSQL=").append(profileSQL);
		if (requireSSL                         != Defaults.REQUIRE_SSL.booVal())                               s.append("&").append("requireSSL=").append(requireSSL);
		if (tcpKeepAlive                       != Defaults.TCP_KEEP_ALIVE.booVal())                            s.append("&").append("tcpKeepAlive=").append(tcpKeepAlive);
		if (tcpNoDelay                         != Defaults.TCP_NO_DELAY.booVal())                              s.append("&").append("tcpNoDelay=").append(tcpNoDelay);
		if (useCursorFetch                     != Defaults.USE_CURSOR_FETCH.booVal())                          s.append("&").append("useCursorFetch=").append(useCursorFetch);
		if (useSSL                             != Defaults.USE_SSL.booVal())                                   s.append("&").append("useSSL=").append(useSSL);
		if (callableStmtCacheSize              != Defaults.CALLABLE_STMT_CACHE_SIZE.intVal())                  s.append("&").append("callableStmtCacheSize=").append(callableStmtCacheSize);
		if (connectTimeout                     != Defaults.CONNECT_TIMEOUT.intVal())                           s.append("&").append("connectTimeout=").append(connectTimeout);
		if (initialTimeout                     != Defaults.INITIAL_TIMEOUT.intVal())                           s.append("&").append("initialTimeout=").append(initialTimeout);
		if (largeRowSizeThreshold              != Defaults.LARGE_ROW_SIZE_THRESHOLD.intVal())                  s.append("&").append("largeRowSizeThreshold=").append(largeRowSizeThreshold);
		if (maxReconnects                      != Defaults.MAX_RECONNECTS.intVal())                            s.append("&").append("maxReconnects=").append(maxReconnects);
		if (maxRows                            != Defaults.MAX_ROWS.intVal())                                  s.append("&").append("maxRows=").append(maxRows);
		if (metadataCacheSize                  != Defaults.METADATA_CACHE_SIZE.intVal())                       s.append("&").append("metadataCacheSize=").append(metadataCacheSize);
		if (prepStmtCacheSize                  != Defaults.PREP_STMT_CACHE_SIZE.intVal())                      s.append("&").append("prepStmtCacheSize=").append(prepStmtCacheSize);
		if (prepStmtCacheSqlLimit              != Defaults.PREP_STMT_CACHE_SQL_LIMIT.intVal())                 s.append("&").append("prepStmtCacheSqlLimit=").append(prepStmtCacheSqlLimit);

		return s.length() > 0 ? s.toString().replaceFirst("&", "?") : "";
	}
}