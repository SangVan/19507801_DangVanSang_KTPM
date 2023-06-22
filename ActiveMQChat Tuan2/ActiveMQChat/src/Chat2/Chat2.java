package Chat2;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.BasicConfigurator;


import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;

public class Chat2 extends JFrame implements MessageListener {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private JTextArea chatArea;
    private JTextField messageField;

    public Chat2() {
      
    	
        setTitle("Chat 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(400, 300);
        setVisible(true);
        initializeMessaging();
    }

    private void initializeMessaging() {
        try {
        	BasicConfigurator.configure();
    		// thiết lập môi trường cho JJNDI
    		Properties settings = new Properties();
    		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
    		settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
    		// tạo context
    		Context ctx = new InitialContext(settings);
    		// lookup JMS connection factory
    		Object obj = ctx.lookup("ConnectionFactory");
    		ConnectionFactory factory = (ConnectionFactory) obj;
    		// lookup destination
    		Destination destination = (Destination) ctx.lookup("dynamicQueues/ChatQueue");
    		// tạo connection
    		Connection con = factory.createConnection("admin", "admin");
    		// nối đến MOM
    		con.start();
    		// tạo session
    		Session session = con.createSession(/* transaction */false, /* ACK */Session.CLIENT_ACKNOWLEDGE);
    		// tạo consumer
    		MessageConsumer receiver = session.createConsumer(destination);
    		// blocked-method for receiving message - sync
    		// receiver.receive();
    		// Cho receiver lắng nghe trên queue, chừng có message thì notify - async
    		System.out.println("Tý was listened on queue...");
    		receiver.setMessageListener(new MessageListener() {

    			// có message đến queue, phương thức này được thực thi
    			public void onMessage(Message msg) {// msg là message nhận được
    				try {
    					if (msg instanceof TextMessage) {
    						TextMessage tm = (TextMessage) msg;
    						String txt = tm.getText();
    						chatArea.append("\nChat2: " + txt);
    						msg.acknowledge();// gửi tín hiệu ack
    					} else if (msg instanceof ObjectMessage) {
    						ObjectMessage om = (ObjectMessage) msg;
    						System.out.println(om);
    					}
    //others message type....
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    			}
    		});
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        try {
        	BasicConfigurator.configure();
    		//config environment for JNDI
    		Properties settings = new Properties();
    		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
    		settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
    		//create context
    		Context ctx = new InitialContext(settings);
    		//lookup JMS connection factory
    		ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
    		//lookup destination. (If not exist-->ActiveMQ create once)
    		Destination destination = (Destination) ctx.lookup("dynamicQueues/ChatQueue");
    		//get connection using credential
    		Connection con = factory.createConnection("admin", "admin");
    		//connect to MOM
    		con.start();
    		//create session
    		Session session = con.createSession(/* transaction */false, /* ACK */Session.AUTO_ACKNOWLEDGE);
    		//create producer
    		MessageProducer producer = session.createProducer(destination);
    		//create text message
    		String messageText = messageField.getText();
            TextMessage message = session.createTextMessage(messageText);

            // Gửi tin nhắn đến hàng đợi
            producer.send(message);

            messageField.setText("");
            
            session.close();
			con.close();
			System.out.println("Finished...");
    		
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
    }

  
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Chat2();
            }
        });
    }


	@Override
	public void onMessage(Message msg) {
		try {
			if (msg instanceof TextMessage) {
				TextMessage tm = (TextMessage) msg;
				String txt = tm.getText();
				chatArea.append("\nChat2: " + txt);
				msg.acknowledge();// gửi tín hiệu ack
			} else if (msg instanceof ObjectMessage) {
				ObjectMessage om = (ObjectMessage) msg;
				System.out.println(om);
			}
//others message type....
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
