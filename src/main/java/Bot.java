import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "NeDushniBot";
    }

    @Override
    public String getBotToken() {
        return "xxx";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            String incomeMsg = EmojiParser.removeAllEmojis(update.getMessage().getText().toLowerCase());
            String chatID = update.getMessage().getChatId().toString();
            int msgID = update.getMessage().getMessageId();
            String userName = update.getMessage().getFrom().getUserName();
            String userLastName = update.getMessage().getFrom().getLastName();
            String userFirstName = update.getMessage().getFrom().getFirstName();

            MessageHandler messageHandler = new MessageHandler(incomeMsg, chatID, msgID, userName, userFirstName, userLastName);
            try {
                execute(messageHandler.handleMessage());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            if (messageHandler.isContainStopWord()) {
                try {
                    execute(DeleteMessage.builder()
                            .chatId(chatID)
                            .messageId(msgID)
                            .build());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

        if (update.hasCallbackQuery()) {
            CallbackQueryHandler callbackQueryHandler = new CallbackQueryHandler(update.getCallbackQuery());

            if (update.getCallbackQuery().getData().equals("yes")) {
                callbackQueryHandler.handleYesCallback();
                if (FileWorker.getYesAnswersCount(update.getCallbackQuery().getMessage().getMessageId()) == 7) {
                    FileWorker.removeFromPollContainer(String.valueOf(update.getCallbackQuery().getMessage().getMessageId()));
                    try {
                        execute(SendMessage.builder()
                                .chatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()))
                                .text("❌По результатам голосования слово \"" + callbackQueryHandler.getStopWord() +
                                        "\" признано душнильским и отправляется в стоп-лист.")
                                .build());
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else if (update.getCallbackQuery().getData().equals("no")) {
                callbackQueryHandler.handleNoCallback();
                if (FileWorker.getNoAnswersCount(update.getCallbackQuery().getMessage().getMessageId()) == 7) {
                    FileWorker.removeFromPollContainer(String.valueOf(update.getCallbackQuery().getMessage().getMessageId()));
                    try {
                        execute(SendMessage.builder()
                                .chatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()))
                                .text("✅По результатам голосования слово\"" + callbackQueryHandler.getStopWord() +
                                        "\" не признано душнильским, используем на здоровье.")
                                .build());
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
