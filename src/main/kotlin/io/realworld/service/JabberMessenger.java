package io.realworld.service;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import java.util.HashMap;
import java.util.Map;

public class JabberMessenger implements Runnable{

    private String nick;
    private String password;
    private String domain;
    private String server;
    private String email;
    private String name;
    private int port;

    private ConnectionConfiguration connConfig;
    private XMPPConnection connection;

    public JabberMessenger (String nick, String password, String domain, String server, int port, String email, String name) throws Exception {
        this.nick = nick;
        this.password = password;
        this.domain = domain;
        this.server = server;
        this.port = port;
        this.email = email;
        this.name = name;
    }

    public void JabberRegistration() throws XMPPException {
        try
        {
            AccountManager accountManager = connection.getAccountManager();
            Map<String, String> attributes = new HashMap<String, String>();
            attributes.put("username", nick);
            attributes.put("password", password);
            attributes.put("email", email);
            attributes.put("name", name);
            accountManager.createAccount( nick, password, attributes );
        }
        catch ( XMPPException e )
        {
            System.out.println( "XMPP account creation failed. Error: " + e.getLocalizedMessage() );
            throw e;
        }
    }

    @Override
    public void run() {
        connConfig = new ConnectionConfiguration(server, port, domain);
        connection = new XMPPConnection(connConfig);
        try {
            JabberRegistration();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        try {
            int priority = 10;
//            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            connection.connect();
            connection.login(nick, password);
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
            presence.setPriority(priority);

            PacketFilter filter = new AndFilter(new PacketTypeFilter(Message.class));

            PacketListener myListener = new PacketListener() {
                public void processPacket(Packet packet) {
                    if (packet instanceof Message) {
                        Message message = (Message) packet;
                        // обработка входящего сообщения
                        processMessage(message);
                    }
                }
            };

            connection.addPacketListener(myListener, filter);

            // раз в минуту проверка соединения
            while(connection.isConnected()) {
                Thread.sleep(60000);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Обработка входящего сообщения<hr>
     * @param message входящее сообщение
     */
    private void processMessage(Message message) {
        String messageBody = message.getBody();
        String JID = message.getFrom();

        /** передача на фронтэенд*/

    }

    /**
     * Отправка сообщения пользователю<hr>
     * @param to JID пользователя, которому надо отправить сообщение<br>
     * @param message сообщение
     */
    private void sendMessage(String to, String message) {
        if(!message.equals("")) {
            ChatManager chatmanager = connection.getChatManager();
            Chat newChat = chatmanager.createChat(to, null);

            try {
                newChat.sendMessage(message);
            }
            catch (XMPPException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void sendMessage(String[] to, String message){
        if(!message.equals("")) {
            for(String character: to){
                ChatManager chatmanager = connection.getChatManager();
                Chat newChat = chatmanager.createChat(character, null);
                try {
                    newChat.sendMessage(message);
                }
                catch (XMPPException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
    }
}
