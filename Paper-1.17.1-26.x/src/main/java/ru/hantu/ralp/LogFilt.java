package ru.hantu.ralp;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogFilt extends AbstractFilter {

    private static final Pattern SENSITIVE_PATTERN = Pattern.compile(
            "(issued server command:\\s*/(?:login|l|register|reg|changepassword)\\b)\\s+.*",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public Result filter(LogEvent event) {
        if (event == null || event.getMessage() == null) {
            return Result.NEUTRAL;
        }
        return checkAndMask(event.getLoggerName(), event.getLevel(), event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return msg == null ? Result.NEUTRAL : checkAndMask(logger.getName(), level, msg.getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return msg == null ? Result.NEUTRAL : checkAndMask(logger.getName(), level, msg.toString());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return checkAndMask(logger.getName(), level, msg);
    }

    private Result checkAndMask(String loggerName, Level level, String message) {
        if (message == null || message.contains("[HIDDEN]")) {
            return Result.NEUTRAL;
        }

        Matcher matcher = SENSITIVE_PATTERN.matcher(message);
        if (matcher.find()) {
            String maskedMessage = matcher.replaceFirst("$1 [HIDDEN]");
            LogManager.getLogger(loggerName != null ? loggerName : "Minecraft").log(level != null ? level : Level.INFO, maskedMessage);
            return Result.DENY;
        }

        return Result.NEUTRAL;
    }
}
