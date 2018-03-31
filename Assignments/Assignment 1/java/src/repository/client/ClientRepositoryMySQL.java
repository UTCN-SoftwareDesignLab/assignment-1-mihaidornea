package repository.client;

import model.Client;
import model.builder.ClientBuilder;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.CLIENT;

public class ClientRepositoryMySQL implements ClientRepository {

    private final Connection connection;

    public ClientRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM client";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                clients.add(getClientFromResultSet(rs));
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Notification<Client> findById(Long id) throws EntityNotFoundException {
        Notification<Client> notification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM client WHERE id=" + id;
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()){
                Client client = getClientFromResultSet(rs);
                notification.setResult(client);
                return notification;
            } else {
                notification.addError("Invalid ID!");
                return notification;
            }

        } catch (SQLException e){
            e.printStackTrace();
            throw new EntityNotFoundException(id, Client.class.getSimpleName());
        }

    }

    @Override
    public Notification<Client> findByPersonalCode(Long personalCode) throws EntityNotFoundException {
        Notification<Client> notification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM client WHERE personalCode=" + personalCode;
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()){
                Client client = getClientFromResultSet(rs);
                notification.setResult(client);
                return notification;
            } else {
                notification.addError("Invalid Personal Code");
                return notification;
            }

        } catch (SQLException e){
            e.printStackTrace();
            throw new EntityNotFoundException(personalCode, Client.class.getSimpleName());
        }
    }

    @Override
    public boolean save(Client client) {
        try{
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT INTO client values(null, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, client.getAddress());
            insertStatement.setLong(2, client.getIdentityNumber());
            insertStatement.setLong(3, client.getPersonalNumericalCode());
            insertStatement.setString(4, client.getName());
            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating client failed, no rows affected");
            }

            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()){
                client.setId(rs.getLong(1));
            } else {
                throw new SQLException("Creating client failed, no ID obtained");
            }
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Client client) {
        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement("UPDATE client SET name = ?, identityNr = ?, personalCode = ?, address = ?  WHERE id = ?");
            updateStatement.setString(1, client.getName());
            updateStatement.setLong(2, client.getIdentityNumber());
            updateStatement.setLong(3, client.getPersonalNumericalCode());
            updateStatement.setString(4, client.getAddress());
            updateStatement.setLong(5, client.getId());
            updateStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean remove(Client client) {
        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement("DELETE FROM client WHERE id = ?");
            updateStatement.setLong(1, client.getId());
            updateStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement("DELETE FROM client WHERE id >= 0");
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Client getClientFromResultSet(ResultSet rs) throws SQLException{
        return new ClientBuilder()
                .setId(rs.getLong("id"))
                .setAddress(rs.getString("address"))
                .setIdentityNumeber(rs.getLong("identityNr"))
                .setPersonalNumericalCode(rs.getLong("personalCode"))
                .setName(rs.getString("name"))
                .build();
    }

}
