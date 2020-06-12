package stl.threebodysimulation;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is a modified version of a class freely provided by user MightyPork, from StackOverflow at https://stackoverflow.com/questions/18004150/desktop-api-is-not-supported-on-the-current-platform.
 * All credit is theirs.
 * No documentation on our part is provided. The code is as-is.
 */
class DesktopAPI {

    /**
     * Opens a directory in the file explorer.
     *
     * @param directoryName The name of the directory.
     */
    static void openDirectory(String directoryName) {
        File directory = new File(directoryName);
        //noinspection ResultOfMethodCallIgnored : as long as there is a directory, what mkdir returns is irrelevant.
        directory.mkdir();
        DesktopAPI.open(directory);
    }

    private static void open(File file) {

        if (openSystemSpecific(file.getPath())) return;

        openDESKTOP(file);
    }


    private static boolean openSystemSpecific(String what) {

        EnumOS os = getOs();

        if (os.isLinux()) {
            if (runCommand("kde-open", what)) return true;
            if (runCommand("gnome-open", what)) return true;
            if (runCommand("xdg-open", what)) return true;
        }

        if (os.isMac()) {
            if (runCommand("open", what)) return true;
        }

        if (os.isWindows()) {
            return runCommand("explorer", what);
        }

        return false;
    }


    private static void openDESKTOP(File file) {

        logOut("Trying to use Desktop.getDesktop().open() with " + file.toString());
        try {
            if (!Desktop.isDesktopSupported()) {
                logErr("Platform is not supported.");
                return;
            }

            if (!Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                logErr("OPEN is not supported.");
                return;
            }

            Desktop.getDesktop().open(file);

        } catch (Throwable t) {
            logErr("Error using desktop open.", t);
        }
    }


    private static boolean runCommand(String command, String file) {

        logOut("Trying to exec:\n   cmd = " + command + "\n   args = " + "%s" + "\n   %s = " + file);

        String[] parts = prepareCommand(command, "%s", file);

        try {
            Process p = Runtime.getRuntime().exec(parts);
            if (p == null) return false;

            try {
                int retval = p.exitValue();
                if (retval == 0) {
                    logErr("Process ended immediately.");
                } else {
                    logErr("Process crashed.");
                }
                return false;
            } catch (IllegalThreadStateException itse) {
                logErr("Process is running.");
                return true;
            }
        } catch (IOException e) {
            logErr("Error running command.", e);
            return false;
        }
    }


    @SuppressWarnings("SameParameterValue")
    private static String[] prepareCommand(String command, String args, String file) {

        List<String> parts = new ArrayList<>();
        parts.add(command);

        if (args != null) {
            for (String s : args.split(" ")) {
                s = String.format(s, file); // put in the filename thing

                parts.add(s.trim());
            }
        }

        //noinspection ToArrayCallWithZeroLengthArrayArgument
        return parts.toArray(new String[parts.size()]);
    }

    private static void logErr(String msg, Throwable t) {
        System.err.println(msg);
        t.printStackTrace();
    }

    private static void logErr(String msg) {
        System.err.println(msg);
    }

    private static void logOut(String msg) {
        System.out.println(msg);
    }

    private static EnumOS getOs() {

        String s = System.getProperty("os.name").toLowerCase();

        if (s.contains("win")) {
            return EnumOS.windows;
        }

        if (s.contains("mac")) {
            return EnumOS.macos;
        }

        if (s.contains("solaris")) {
            return EnumOS.solaris;
        }

        if (s.contains("sunos")) {
            return EnumOS.solaris;
        }

        if (s.contains("linux")) {
            return EnumOS.linux;
        }

        if (s.contains("unix")) {
            return EnumOS.linux;
        } else {
            return EnumOS.unknown;
        }
    }


    private enum EnumOS {
        linux, macos, solaris, unknown, windows;

        private boolean isLinux() {

            return this == linux || this == solaris;
        }


        private boolean isMac() {

            return this == macos;
        }


        private boolean isWindows() {

            return this == windows;
        }
    }
}