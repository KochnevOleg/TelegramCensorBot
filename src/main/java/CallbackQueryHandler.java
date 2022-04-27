import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public class CallbackQueryHandler {

    private final CallbackQuery callbackQuery;

    public CallbackQueryHandler(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    public void handleYesCallback() {
        String stopWord = callbackQuery.getMessage().getText().split(" ")[7];

        String pollResult = FileWorker.getPollResult(callbackQuery.getMessage().getMessageId().toString());
        String [] pollResultSplit = pollResult.split(" ");

            int yesCallback = Integer.parseInt(pollResultSplit[1]);
            yesCallback++;
            pollResultSplit[1] = String.valueOf(yesCallback);

            String pollResultToWrite = pollResultSplit[0] + " " + pollResultSplit[1] + " " + pollResultSplit[2];

            FileWorker.pollContainerUpdate(pollResultToWrite);

        if(yesCallback == 7){
            FileWorker.toStopListTransfer(stopWord);
            FileWorker.removeFromPollContainer(pollResult);
        }
    }

    public void handleNoCallback() {
        String stopWord = callbackQuery.getMessage().getText().split(" ")[7];
        String[] pollResultSplit = FileWorker.getPollResult(callbackQuery.getMessage().getMessageId().toString()).split(" ");

            int noCallback = Integer.parseInt(pollResultSplit[2]);
        noCallback++;
            pollResultSplit[2] = String.valueOf(noCallback);

            if (noCallback == 7) {
                FileWorker.removeFromTargetTags(stopWord);
            }
        }

        public String getStopWord() {
        String[] msg = callbackQuery.getMessage().getText().split(" ");
        return msg[7];
        }
    }

