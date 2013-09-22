package com.sohail.alam.http.common;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 22/9/13
 * Time: 10:17 PM
 */
public class Constants {

    /**
     * Welcome screen.
     *
     * @return the string
     */
    public static String welcomeScreen() {
        StringBuilder builder = new StringBuilder();
        builder
                .append(" ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  \n")
                .append("(______)(______)(______)(______)(______)(______)(______)(______)(______)(______)(______)(______)(______)(______)(______) \n")
                .append("                                                                                                                         \n")
                .append("                      _       _         _                                       _____                                    \n")
                .append("                     ( )  _  ( )       (_ )                                    (_   _)                                   \n")
                .append("                     | | ( ) | |   __   | |    ___    _     ___ ___     __       | |     _                               \n")
                .append("                     | | | | | | /'__`\\ | |  /'___) /'_`\\ /' _ ` _ `\\ /'__`\\     | |   /'_`\\                             \n")
                .append("                     | (_/ \\_) |(  ___/ | | ( (___ ( (_) )| ( ) ( ) |(  ___/     | |  ( (_) )                            \n")
                .append("                     `\\___x___/'`\\____)(___)`\\____)`\\___/'(_) (_) (_)`\\____)     (_)  `\\___/'                            \n")
                .append("                                                                                                                         \n")
                .append("                                                                                                                         \n")
                .append(" _   _  _____  _____  ___       ___                                               __     ___                             \n")
                .append("( ) ( )(_   _)(_   _)(  _`\\    (  _`\\                                            /  )   (  _`\\                           \n")
                .append("| |_| |  | |    | |  | |_) )   | (_(_)   __   _ __  _   _    __   _ __         /' /'    | |_) ) _ __    _          _   _ \n")
                .append("|  _  |  | |    | |  | ,__/'   `\\__ \\  /'__`\\( '__)( ) ( ) /'__`\\( '__)      /' /'      | ,__/'( '__) /'_`\\ (`\\/')( ) ( )\n")
                .append("| | | |  | |    | |  | |       ( )_) |(  ___/| |   | \\_/ |(  ___/| |       /' /'        | |    | |   ( (_) ) >  < | (_) |\n")
                .append("(_) (_)  (_)    (_)  (_)       `\\____)`\\____)(_)   `\\___/'`\\____)(_)      (_/'          (_)    (_)   `\\___/'(_/\\_)`\\__, |\n")
                .append("                                                                                                                  ( )_| |\n")
                .append("                                                                                                                  `\\___/'\n")
                .append(developer())
                .append("                                                                                                                         \n")
                .append(" ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  ______  \n")
                .append("(______)(______)(______)(______)(______)(______)(______)(______)(______)(______)(______)(______)(______)(______)(______) \n")
                .append("                                                                                                                         \n");

        return builder.toString();
    }

    /**
     * Developer string.
     *
     * @return the string
     */
    public static String developer() {
        StringBuilder builder = new StringBuilder();
        builder
                .append(" _                 _           _     o     __                   _          \n")
                .append("| \\ _     _  |  _ |_) _  _|   |_) \\/      (_  _ |_  _  o  |    |_| |  _ __ \n")
                .append("|_/(/_\\_/(/_ | (_)|  (/_(_|   |_) /  o    __)(_)| |(_| |  |    | | | (_||||");

        return builder.toString();
    }

    /**
     * License string.
     *
     * @return the string
     */
    public static String license() {
        StringBuilder builder = new StringBuilder();

        return builder.toString();
    }
}
