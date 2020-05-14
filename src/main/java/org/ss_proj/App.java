package org.ss_proj;

import picocli.CommandLine;

import java.util.Locale;

@CommandLine.Command(subcommands = { org.ss_proj.htmlchecker.App.class, org.ss_proj.lowvision.App.class })
public class App {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new App())
                .registerConverter(Locale.class, s -> new Locale.Builder().setLanguageTag(s).build())
                .execute(args);
        System.exit(exitCode);
    }
}
