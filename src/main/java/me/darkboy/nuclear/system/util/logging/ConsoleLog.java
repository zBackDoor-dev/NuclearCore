package me.darkboy.nuclear.system.util.logging;

import me.darkboy.nuclear.system.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * @author NotStxnks_
 */
public class ConsoleLog {

    private String logFormat = "[&3Nuclear&bCore&r] [{nuclearcore_log_level}]: {nuclearcore_log_message}";

    /**
     * @param message the message to log.
     * @param args    arguments referenced by the format specifiers
     *                in the format string.
     */
    public void log(String message, Object... args) {
        Bukkit.getConsoleSender().sendMessage(String.format(message, args));
    }

    /**
     * @param messages the messages to log.
     */
    public void log(String... messages) {
        Arrays.asList(messages).forEach(this::log);
    }

    /**
     * @param messages the messages to log.
     */
    public void log(Iterable<? extends String> messages) {
        messages.forEach(this::log);
    }

    /**
     * @param level   severity of the log.
     * @param message the message to log.
     * @param args    arguments referenced by the format specifiers
     *                in the format string.
     */
    public void log(ConsoleLevel level, String message, Object... args) {
        log(Messages.colour(logFormat
                .replace("{nuclearcore_log_level}", level.getTagColour() + level.getTag() + ChatColor.RESET)
                .replace("{nuclearcore_log_message}", message)), args);
    }

    /**
     * @param message the message to debug.
     * @param args    arguments referenced by the format specifiers
     *                in the format string.
     */
    public void debug(String message, Object... args) {
        log(ConsoleLevel.DEBUG, message, args);
    }

    /**
     * @param message the message to inform.
     * @param args    arguments referenced by the format specifiers
     *                in the format string.
     */
    public void info(String message, Object... args) {
        log(ConsoleLevel.INFO, message, args);
    }

    /**
     * @param message the message to warn.
     * @param args    arguments referenced by the format specifiers
     *                in the format string.
     */
    public void warn(String message, Object... args) {
        log(ConsoleLevel.WARN, message, args);
    }

    /**
     * @param throwable the throwable to output.
     */
    public void exception(Throwable throwable) {
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));

        Arrays.asList(writer.toString()
                .split(System.lineSeparator()))
                .forEach(line -> log(ConsoleLevel.EXCEPTION, line));
    }

    /**
     * @param message the message to error.
     * @param args    arguments referenced by the format specifiers
     *                in the format string.
     */
    public void error(String message, Object... args) {
        log(ConsoleLevel.ERROR, message, args);
    }

    public String getFormat() {
        return logFormat;
    }

    public void setFormat(String logFormat) {
        this.logFormat = logFormat;
    }
}
