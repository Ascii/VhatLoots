/*
 * VhatLoots
 * Copyright (C) 2019 Lukas Mansour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.articdive.vhatloots.helpers;

import de.articdive.vhatloots.VhatLoots;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukas Mansour
 */
public class LoggingHelper {
    public static void setupLogger(VhatLoots main) {
        //Log4j2
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
    
        List<Appender> appenders = new ArrayList<>();
        List<AppenderRef[]> appenderReferences = new ArrayList<>();
        Layout<String> layout = PatternLayout.newBuilder()
                .withPattern(PatternLayout.SIMPLE_CONVERSION_PATTERN)
                .withPatternSelector(null)
                .withRegexReplacement(null)
                .withCharset(null)
                .withAlwaysWriteExceptions(false)
                .withNoConsoleNoAnsi(false)
                .withHeader(null)
                .withFooter(null)
                .withConfiguration(config)
                .build();
    
        appenders.add(FileAppender.newBuilder().
                withFileName(main.getDataFolder() + File.separator + "logs" + File.separator + "ActiveJDBC.log")
                .withAppend(true)
                .withLocking(false)
                .withName("FileActiveJDBC")
                .withImmediateFlush(true)
                .withIgnoreExceptions(false)
                .withBufferedIo(false)
                .withBufferSize(0)
                .withLayout(layout)
                .withAdvertise(false)
                .setConfiguration(config)
                .withBufferedIo(false)
                .build());
        appenders.add(FileAppender.newBuilder().
                withFileName(main.getDataFolder() + File.separator + "logs" + File.separator + "Liquibase.log")
                .withAppend(true)
                .withLocking(false)
                .withName("FileLiquibase")
                .withImmediateFlush(true)
                .withIgnoreExceptions(false)
                .withBufferedIo(false)
                .withBufferSize(0)
                .withLayout(layout)
                .withAdvertise(false)
                .setConfiguration(config)
                .build());
    
        for (Appender appender : appenders) {
            appender.start();
            config.addAppender(appender);
            appenderReferences.add(new AppenderRef[]{AppenderRef.createAppenderRef(appender.getName(), Level.ALL, null)});
        }
    
        LoggerConfig activeJDBCConf = LoggerConfig.createLogger(false, Level.ALL, "activejdbc", "false", appenderReferences.get(0), null, config, null);
        activeJDBCConf.addAppender(appenders.get(0), Level.ALL, null);
        LoggerConfig liquibaseConf = LoggerConfig.createLogger(false, Level.ALL, "liquibase", "false", appenderReferences.get(1), null, config, null);
        liquibaseConf.addAppender(appenders.get(1), Level.ALL, null);
    
        config.addLogger("org.javalite", activeJDBCConf);
        config.addLogger("liquibase", liquibaseConf);
        ctx.updateLoggers();
    }
}
