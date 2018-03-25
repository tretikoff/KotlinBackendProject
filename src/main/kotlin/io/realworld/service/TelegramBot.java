package io.realworld.service;

import io.realworld.model.User;
import io.realworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Controller
public class TelegramBot {

    @Autowired
    public static UserService userService;
    @Autowired
    public static UserRepository userRepository;
    public static String email;
    public static String password;


    public static void main(String... args) throws Exception {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(new TelegramLongPollingBot() {


            @Override
            public void onUpdateReceived(Update update) {
                Message msg = update.getMessage();
                String txt = msg.getText();
                if (txt.equals("/start"))
                    sendMsg(msg, "Please, take your email");
                else if (txt.equals("/help")) {
                    sendMsg(msg, " /showNote - добавить заметку");
                } else if (txt.substring(0, 6).equals("/email")) {
                    checkLogin(msg.getText().substring(7));
                    checkUser(msg);
//                    sendMsg(msg,"Please, take your password" );
                } else if (txt.substring(0, 9).equals("/password")) {
                    checkPassword(msg.getText().substring(10));
                    sendMsg(msg, "Wait a minute...");
                    checkUser(msg);
                } else if (txt.substring(0, 9).equals("/showNote")) {
                    showFirstNote(msg, msg.getText().substring(9));
                }
            }

            @Override
            public String getBotUsername() {
                return "my_hogwarts_bot";
            }

            @Override
            public String getBotToken() {
                return "522531769:AAH4ZakfyfqR4bSqw6RfQ3EHCG3Z4Md8xNA";
            }

            private void sendMsg(Message msg, String text) {
                SendMessage s = new SendMessage();
                s.setChatId(msg.getChatId().toString()); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
                s.setReplyToMessageId(msg.getMessageId());
                s.setText(text);
                try {
                    sendMessage(s);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            private void showNews(Message msg) {
                SendMessage s = new SendMessage();
                s.setChatId(msg.getChatId());
                //        CharactersEntity characterEntity = charactersService.findByChatId(msg.getChatId());
                //        MembersEntity membersEntity = membersService.findByCharacter(characterEntity);
                //        GroupsEntity groupsEntity = groupsService.
            }

            private void showFirstNote(Message msg, String note) {
                try {
                    User user = userRepository.findByChatId(msg.getChatId());
//                    List<Note> notes = noteRepository.findAllByUser(user);
//                    sendMsg(msg, notes.get(0).getBody());
                    sendMsg(msg, "The note saved!");
                } catch (Exception e) {
                    sendMsg(msg, "Please, send login and password");
                }
            }

            private void checkLogin(String email2) {
                //        Long chatId = msg.getChatId();
                email = email2;
            }

            private void checkPassword(String password2) {
                password = password2;
            }

            private void checkUser(Message msg) {
//                try {
                User user = userRepository.findByEmail("admin@mail.ru");
                sendMsg(msg, "You are welcome!");
                Long chatId = msg.getChatId();
                user.setChatId(chatId);
//                }catch (Exception e){
//                    sendMsg(msg, "You have a trouble with email");
//                }
            }
        });
    }
//    @Autowired
//    UserService userService;
//    @Autowired
//    EventRepository timetablesService;
//    @Autowired
//    GroupRepository newsService;
//    @Autowired
//    NoteRepository membersService;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    NoteRepository noteRepository;
//
//    private String email;
//    private String password;
//
//    public static void main(String[] args){
//        ApiContextInitializer.init();
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//        try {
//            telegramBotsApi.registerBot(new TelegramBot());
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void onUpdateReceived(Update update) {
//        Message msg = update.getMessage();
//        String txt = msg.getText();
//        if(txt.equals("/start"))
//            sendMsg(msg,"Please, take your login" );
//        if(txt.substring(0,5).equals("/email")){
//            checkLogin(msg.getText().substring(6));
//            sendMsg(msg,"Please, take your password" );
//        }
//        if(txt.substring(0,8).equals("/password")){
//            checkPassword(msg.getText().substring(9));
//            checkUser(msg);
//        }
//        if(txt.substring(0,7).equals("/showNote")){
//            showFirstNote(msg, msg.getText().substring(9));
//            sendMsg(msg,"The note saved!");
//        }
//        if(txt.equals("/help")){
//            sendMsg(msg, " /showNote - добавить заметку");
//        }
//    }
////"Hello, Guy! Welcome to Hogwarts! Write /man for help."
//    @Override
//    public String getBotUsername() {
//        return "my_hogwarts_bot";
//    }
//
//    @Override
//    public String getBotToken() {
//        return "522531769:AAH4ZakfyfqR4bSqw6RfQ3EHCG3Z4Md8xNA";
//    }
//
//    private void sendMsg(Message msg, String text) {
//        SendMessage s = new SendMessage();
//        s.setChatId(msg.getChatId().toString()); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
//        s.setReplyToMessageId(msg.getMessageId());
//        s.setText(text);
//        try {
//            sendMessage(s);
//        } catch (TelegramApiException e){
//            e.printStackTrace();
//        }
//    }
//
//    private void showNews(Message msg){
//        SendMessage s = new SendMessage();
//        s.setChatId(msg.getChatId());
////        CharactersEntity characterEntity = charactersService.findByChatId(msg.getChatId());
////        MembersEntity membersEntity = membersService.findByCharacter(characterEntity);
////        GroupsEntity groupsEntity = groupsService.
//    }
//
//    private void showFirstNote(Message msg, String note){
//        User user = userRepository.findByChatId(msg.getChatId());
//        if(user == null){
//            sendMsg(msg, "Please, send login and password");
//        } else{
//            List<Note> notes = noteRepository.findAllByUser(user);
//            sendMsg(msg, notes.get(0).getBody());
//        }
//    }
//
//    private void checkLogin(String email){
////        Long chatId = msg.getChatId();
//        this.email = email;
//
//    }
//    private void checkPassword(String password){
//        this.password = password;
//    }
//
//    private void checkUser(Message msg){
//        User user = userRepository.findByEmailAndPassword(email,password);
//        if(user == null){
//            sendMsg(msg, "You have a trouble with email or password");
//        }else{
//            sendMsg(msg, "You are welcome!");
//            Long chatId = msg.getChatId();
//            user.setChatId(chatId);
//        }
//    }
}