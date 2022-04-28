import java.io.*;
import java.util.*;

public class FileWorker {


    protected static Set<String> getTargetTags() {
        Set<String> targetTags = new TreeSet<>();
        String path = "targetTags.txt";
        File file = new File(path);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                targetTags.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return targetTags;
    }


    protected static Set<String> getStopList() {
        Set<String> stopList = new TreeSet<>();
        String path = "stopList.txt";
        File file = new File(path);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                stopList.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stopList;
    }


    protected static String getTargetTag(String msg) {
        String[] splitMsg = msg.split("[ ?,:;!]");
        String targetTag = null;
        for (String s : splitMsg) {
            if (getTargetTags().contains(s)) {
                targetTag = s;
            }
        }
        return targetTag;
    }

    protected static void toStopListTransfer(String stopWord) {
        Set<String> stopList = new TreeSet<>();
        String path = "stopList.txt";
        File file = new File(path);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                stopList.add(scanner.nextLine());
            }
            stopList.add(stopWord);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (FileWriter fw = new FileWriter(path)) {
            for (String word : stopList) {
                fw.write(word + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<String> targetTags = new TreeSet<>();
        String pathTarget = "targetTags.txt";
        File targetTagsFile = new File(pathTarget);

        try (Scanner scanner1 = new Scanner(targetTagsFile)) {
            while (scanner1.hasNextLine()) {
                targetTags.add(scanner1.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        targetTags.removeIf(word -> word.equals(stopWord));

        try (FileWriter fw1 = new FileWriter(pathTarget)) {
            for (String word : targetTags) {
                fw1.write(word + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected static void toPollContainerWriter(int K) {
        String path = "pollContainer.txt";

        try (FileWriter fw = new FileWriter(path)){
            String newPoll = K + " " + 0 + " " + 0;
            fw.write(newPoll);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected static String getPollResult(String msgID) {
        String pollResult = null;
        String path = "pollContainer.txt";
        File file = new File(path);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                String[] split = nextLine.split(" ");
                if (split[0].equals(msgID)) {
                    pollResult = nextLine;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pollResult;
    }

    protected static void pollContainerUpdate(String pollResultToWrite) {
        Set<String> pollContainer = new TreeSet<>();
        String path = "pollContainer.txt";
        File file = new File(path);


        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                pollContainer.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String[] toWriteSplit = pollResultToWrite.split(" ");

        pollContainer.removeIf(string -> string.startsWith(toWriteSplit[0]));

        pollContainer.add(pollResultToWrite);

        try (FileWriter fw = new FileWriter(path)) {
            for (String string : pollContainer) {
                fw.write(string + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void removeFromPollContainer(String pollResult) {
        Set<String> pollContainer = new TreeSet<>();
        String path = "pollContainer.txt";
        File file = new File(path);

        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()) {
                pollContainer.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String[] toRemoveSplit = pollResult.split(" ");
        pollContainer.removeIf(string -> string.startsWith(toRemoveSplit[0]));

        try (FileWriter fw = new FileWriter(path)) {
            for (String string : pollContainer) {
                fw.write(string + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeFromTargetTags(String stopWord) {
        Set<String> targetTags = new TreeSet<>();
        String path = "targetTags.txt";
        File file = new File(path);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                targetTags.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        targetTags.remove(stopWord);

        try (FileWriter fw = new FileWriter(path)) {
            for (String string : targetTags) {
                fw.write(string + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static int getYesAnswersCount(int msgID) {
        String[] pollResult = getPollResult(String.valueOf(msgID)).split(" ");
        return Integer.parseInt(pollResult[1]);
    }

    protected static int getNoAnswersCount(int msgID) {
        String[] pollResult = getPollResult(String.valueOf(msgID)).split("");
        return Integer.parseInt(pollResult[2]);
    }
}
