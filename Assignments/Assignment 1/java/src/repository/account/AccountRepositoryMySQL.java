package repository.account;

import model.Account;
import model.User;
import model.builder.AccountBuilder;
import repository.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryMySQL implements AccountRepository {

    private final Connection connection;

    public AccountRepositoryMySQL (Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM account";
            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()){
                accounts.add(getAccountFromResultSet(rs));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account findById(Long id) throws EntityNotFoundException {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM account WHERE id=" + id;
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return getAccountFromResultSet(rs);
            } else {
                throw new EntityNotFoundException(id, Account.class.getSimpleName());
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new EntityNotFoundException(id, Account.class.getSimpleName());
        }
    }

    @Override
    public boolean save(Account account) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT INTO account values(null, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertStatement.setLong(1, account.getClient_id());
            insertStatement.setLong(2, account.getIdentificationNumber());
            insertStatement.setString(3, account.getType());
            insertStatement.setLong(4, account.getAmountOfMoney());
            insertStatement.setDate(5, new java.sql.Date(account.getDateOfCreation().getTime()));
            int colschanged = insertStatement.executeUpdate();
            if (colschanged == 0) {
                throw new SQLException("Creating account failed, no rows affected");
            }

            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()){
                account.setId(rs.getLong(1));
            } else {
                throw new SQLException("Creating account failed, no ID obtained");
            }
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Account account){
        try{
            PreparedStatement insertStatement = connection
                    .prepareStatement("UPDATE account SET money = ? WHERE id = ?");
            insertStatement.setLong(1, account.getAmountOfMoney());
            insertStatement.setLong(2, account.getId());
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        try{
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM account WHERE id >= 0";
            statement.executeUpdate(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private Account getAccountFromResultSet(ResultSet rs) throws SQLException{
        return new AccountBuilder()
                .setId(rs.getLong("id"))
                .setIdentificationNumber(rs.getLong("identificationNr"))
                .setAmountOfMoney(rs.getLong("money"))
                .setDateOfCreation(rs.getDate("date"))
                .setType(rs.getString("type"))
                .setClientId(rs.getLong("client_id"))
                .build();
    }

}
