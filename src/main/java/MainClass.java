
import java.util.List;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.naming.Context;


public class MainClass {
    private static SessionFactory factory;

    public static void main(String[] args) {

        try {
            System.getProperty(Context.PROVIDER_URL);
            factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        MainClass ME = new MainClass();

        /* Add client to the database */
        Integer clientID1 = ME.addClient(1, "Patryk", "Modny");
        Integer clientID2 = ME.addClient(2, "Daisy", "Das");
        Integer clientID3 = ME.addClient(3, "John", "Paul");

        /* List all client's */
        ME.listClients();

        /* Update client's records */
        ME.updateClient(clientID1, "Daniel", null);

        /* Delete an client from the database */
        ME.deleteClient(clientID3);

        /* List all client's */
        ME.listClients();
    }


    /* Method to CREATE an client in the database */
    public Integer addClient(Integer id, String name, String surname) {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer clientID = null;
        try {
            tx = session.beginTransaction();
            Client client = new Client(id, name, surname);
            clientID = (Integer) session.save(client);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return clientID;
    }

    /* Method to  READ all the client's */
    public void listClients() {
        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List clients = session.createQuery("FROM Client").list();
            for (Iterator iterator = clients.iterator(); iterator.hasNext(); ) {
                Client client = (Client) iterator.next();
                System.out.print("OUTPUT: Name: " + client.getName() + " Surname: " + client.getSurname() + "\n");
            }
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /* Method to UPDATE client */
    public void updateClient(Integer ClientID, String name, String surname) {
        Session session = factory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Client client = session.get(Client.class, ClientID);
            if (name != null && !name.equals(""))
                client.setName(name);
            if (surname != null && !surname.equals(""))
                client.setSurname(surname);
            session.update(client);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /* Method to DELETE an client from the records */
    public void deleteClient(Integer clientID) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Client client = session.get(Client.class, clientID);
            session.delete(client);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}