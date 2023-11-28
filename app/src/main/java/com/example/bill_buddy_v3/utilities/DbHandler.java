package com.example.bill_buddy_v3.utilities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bill_buddy_v3.model.Bill;
import com.example.bill_buddy_v3.model.Frequency;
import com.example.bill_buddy_v3.model.Type;
import com.example.bill_buddy_v3.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "billbuddy";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_BILLS = "bills";
    private static final String TABLE_TYPE = "type";
    private static final String TABLE_FREQUENCY = "frequency";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOC = "location";
    private static final String KEY_DESG = "designation";

    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "birth TEXT,"
                + "email TEXT,"
                + "password TEXT"
                + ")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_TYPE_TABLE = "CREATE TABLE " + TABLE_TYPE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "type TEXT"
                + ")";
        db.execSQL(CREATE_TYPE_TABLE);

        String CREATE_FREQUENCY_TABLE = "CREATE TABLE " + TABLE_FREQUENCY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "frequency TEXT"
                + ")";
        db.execSQL(CREATE_FREQUENCY_TABLE);

        String CREATE_BILLS_TABLE = "CREATE TABLE " + TABLE_BILLS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type INTEGER, " +
                "amount NUMERIC, " +
                "payee TEXT, " +
                "due_date TEXT, " +
                "frequency TEXT, " +
                "payed NUMERIC, " +
                "payment_date TEXT," +
                "user_id INTEGER," +
                "FOREIGN KEY(type) REFERENCES " + TABLE_TYPE + "(" + KEY_ID + ")," +
                "FOREIGN KEY(frequency) REFERENCES " + TABLE_FREQUENCY + "(" + KEY_ID + ")," +
                "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")" +
                ")";
        db.execSQL(CREATE_BILLS_TABLE);

        //populateInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FREQUENCY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);

        // Create tables again
        onCreate(db);

    }

    // Insert New User
    public void addNewUser (User user) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put ("name", user.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cValues.put ("birth", sdf.format(user.getBirth()));
        cValues.put ("email", user.getEmail());
        cValues.put ("password", user.getPassword());

        db.insert(TABLE_USERS,null, cValues);
        db.close();
    }

    // Find user By E-mail
    @SuppressLint("Range")
    public User getUserByMail (String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        User user = new User();

        String query = "SELECT id, name, birth, email, password FROM " + TABLE_USERS +
                " where email = '" + email + "'";

        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            try{
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                user.setBirth(sdf.parse(cursor.getString(cursor.getColumnIndex("birth"))));
            } catch (Exception e) {
                System.out.println("Parse error " + e.getMessage());
            }
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        }
        cursor.close();
        return user;
    }

    // Insert Bill
    public void addNewBill (Bill bill) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put ("type", bill.getType().getId());
        cValues.put ("amount", bill.getAmount());
        cValues.put ("payee", bill.getPayee());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cValues.put ("due_date", sdf.format(bill.getDue_date()));
        cValues.put ("frequency", bill.getFrequency().getId());
        cValues.put ("payed", false);
        cValues.put ("user_id", bill.getUser_id());

        db.insert(TABLE_BILLS,null, cValues);
        db.close();
    }

    // Find All Not Payed Bills
    @SuppressLint("Range")
    public ArrayList<Bill> getAllNotPayedBills (int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Bill> billList = new ArrayList<>();

        String query = "SELECT b.id, b.amount, b.due_date, b.payee, b.user_id, t.id AS type_id, t.type AS type_name, f.id AS frequency_id, f.frequency AS frequency_name" +
                " FROM " + TABLE_BILLS + " b, " + TABLE_TYPE + " t, " + TABLE_FREQUENCY + " f " +
                " WHERE b.type = t.id" +
                " AND b.frequency = f.id " +
                " AND b.payed = false " +
                " AND b.user_id = " + user_id +
                " ORDER BY b.due_date ";

        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.setId(cursor.getInt(cursor.getColumnIndex("id")));
                bill.setType( new Type(cursor.getInt(cursor.getColumnIndex("type_id")), cursor.getString(cursor.getColumnIndex("type_name"))));
                bill.setAmount(cursor.getDouble(cursor.getColumnIndex("amount")));
                bill.setPayee(cursor.getString(cursor.getColumnIndex("payee")));
                try{
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    bill.setDue_date(sdf.parse(cursor.getString(cursor.getColumnIndex("due_date"))));
                } catch (Exception e) {
                    System.out.println("Parse error " + e.getMessage());
                }
                bill.setFrequency( new Frequency(cursor.getInt(cursor.getColumnIndex("frequency_id")), cursor.getString(cursor.getColumnIndex("frequency_name"))));
                bill.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));

                billList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return billList;
    }

    // Find All Payed Bills
    @SuppressLint("Range")
    public ArrayList<Bill> getAllPayedBills (int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Bill> billList = new ArrayList<>();

        String query = "SELECT b.id, b.amount, b.due_date, b.payee, b.user_id, b.payment_date, t.id AS type_id, t.type AS type_name, f.id AS frequency_id, f.frequency AS frequency_name" +
                " FROM " + TABLE_BILLS + " b, " + TABLE_TYPE + " t, " + TABLE_FREQUENCY + " f " +
                " WHERE b.type = t.id" +
                " AND b.frequency = f.id " +
                " AND b.payed = true " +
                " AND b.user_id = " + user_id +
                " ORDER BY b.payment_date DESC ";

        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.setId(cursor.getInt(cursor.getColumnIndex("id")));
                bill.setType( new Type(cursor.getInt(cursor.getColumnIndex("type_id")), cursor.getString(cursor.getColumnIndex("type_name"))));
                bill.setAmount(cursor.getDouble(cursor.getColumnIndex("amount")));
                bill.setPayee(cursor.getString(cursor.getColumnIndex("payee")));
                try{
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    bill.setDue_date(sdf.parse(cursor.getString(cursor.getColumnIndex("due_date"))));
                    bill.setPayment_date(sdf.parse(cursor.getString(cursor.getColumnIndex("payment_date"))));
                } catch (Exception e) {
                    System.out.println("Parse error " + e.getMessage());
                }
                bill.setFrequency( new Frequency(cursor.getInt(cursor.getColumnIndex("frequency_id")), cursor.getString(cursor.getColumnIndex("frequency_name"))));
                //bill.setPayed(cursor.getInt(cursor.getColumnIndex("payed")) > 0);
                bill.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));

                billList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return billList;
    }

    // Update Bill
    public int updateBillPaymentDate(Bill bill){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        cVals.put("payed", true);
        cVals.put("payment_date", sdf.format(bill.getPayment_date()).toString());
        int count = db.update(TABLE_BILLS, cVals, KEY_ID + " = ?", new String[]{String.valueOf(bill.getId())});
        return  count; //return the number of rows that were updated
    }

    // Find All Types
    @SuppressLint("Range")
    public ArrayList<Type> getAllTypes () {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Type> typeList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_TYPE;

        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                Type type = new Type();
                type.setId(cursor.getInt(cursor.getColumnIndex("id")));
                type.setType(cursor.getString(cursor.getColumnIndex("type")));
                typeList.add(type);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return typeList;
    }

    // Insert New Type
    public Long addNewType (Type type) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put ("type", type.getType());

        Long code = db.insert(TABLE_TYPE,null, cValues);
        db.close();
        return code;
    }

    // Find All Frequency
    @SuppressLint("Range")
    public ArrayList<Frequency> getAllFrequency () {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Frequency> frequencyList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_FREQUENCY;

        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                Frequency frequency = new Frequency();
                frequency.setId(cursor.getInt(cursor.getColumnIndex("id")));
                frequency.setFrequency(cursor.getString(cursor.getColumnIndex("frequency")));
                frequencyList.add(frequency);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return frequencyList;
    }

    // Insert New Frequency
    public void addNewFrequency (Frequency frequency) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put ("frequency", frequency.getFrequency());

        db.insert(TABLE_FREQUENCY,null, cValues);
        db.close();
    }

    public void populateInitialData () {
        //SQLiteDatabase db
        SQLiteDatabase db = this.getWritableDatabase();
        String INSERT_FREQUENCY = "insert into frequency values (1, \"weekly\")";
        db.execSQL(INSERT_FREQUENCY);
        INSERT_FREQUENCY = "insert into frequency values (2, \"bi-weekly\")";
        db.execSQL(INSERT_FREQUENCY);
        INSERT_FREQUENCY = "insert into frequency values (3, \"monthly\")";
        db.execSQL(INSERT_FREQUENCY);
        INSERT_FREQUENCY = "insert into frequency values (4, \"anually\")";
        db.execSQL(INSERT_FREQUENCY);

        String INSERT_TYPE = "insert into type values (1, \"rent\")";
        db.execSQL(INSERT_TYPE);
        INSERT_TYPE = "insert into type values (2, \"electricity\")";
        db.execSQL(INSERT_TYPE);
        INSERT_TYPE = "insert into type values (3, \"college\")";
        db.execSQL(INSERT_TYPE);

        /*String INSERT_USER = "insert into users (id, name, birth, email, password) values (1, \"Alana\", \"1987-11-14\", \"alana@email.com\", \"senha123\")";
        db.execSQL(INSERT_USER);
        INSERT_USER = "insert into users (id, name, birth, email, password) values (2, \"Bruno\", \"1987-11-14\", \"bruno@email.com\", \"senha123\")";
        db.execSQL(INSERT_USER);
        INSERT_USER = "insert into users (id, name, birth, email, password) values (3, \"Carlos\", \"1987-11-14\", \"carlos@email.com\", \"senha123\")";
        db.execSQL(INSERT_USER);


        // Bills User 1
        String INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id) values (1, 3, 1000.0, \"Douglas\", \"2023-12-04\", 1, 0, 1)";
        db.execSQL(INSERT_BILL);
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id) values (2, 1, 2500.0, \"House\", \"2023-12-22\", 3, 0, 1)";
        db.execSQL(INSERT_BILL);
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id, payment_date) values (3, 2, 2500.0, \"Gym\", \"2023-11-20\", 1, 1, 1, \"2023-11-14\")";
        db.execSQL(INSERT_BILL);
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id) values (4, 2, 2500.0, \"BCHydro\", \"2023-12-01\", 1, 0, 1)";
        db.execSQL(INSERT_BILL);
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id, payment_date) values (5, 1, 20.0, \"Walmart\", \"2023-11-29\", 2, 1, 1, \"2023-11-25\")";
        db.execSQL(INSERT_BILL);
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id) values (6, 3, 250.0, \"Best Buy\", \"2023-12-15\", 1, 0, 1)";
        db.execSQL(INSERT_BILL);
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id, payment_date) values (7, 2, 500.0, \"Tellus\", \"2023-11-19\", 2, 1, 1, \"2023-11-19\")";
        db.execSQL(INSERT_BILL);

        // Bills User 2
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id) values (8, 2, 2500.0, \"BCHydro 2\", \"2023-12-01\", 1, 0, 2)";
        db.execSQL(INSERT_BILL);
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id, payment_date) values (9, 1, 20.0, \"Walmart 2\", \"2023-11-29\", 2, 2, 2, \"2023-11-25\")";
        db.execSQL(INSERT_BILL);
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id) values (10, 3, 250.0, \"Best Buy 2\", \"2023-12-15\", 1, 0, 2)";
        db.execSQL(INSERT_BILL);

        // Bills User 3
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id) values (11, 3, 1000.0, \"Douglas 3\", \"2023-12-04\", 1, 0, 3)";
        db.execSQL(INSERT_BILL);
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id) values (12, 1, 2500.0, \"House 3\", \"2023-12-22\", 3, 0, 3)";
        db.execSQL(INSERT_BILL);
        INSERT_BILL = "insert into bills (id, type, amount, payee, due_date, frequency, payed, user_id, payment_date) values (13, 2, 2500.0, \"Gym 3\", \"2023-11-20\", 1, 1, 3, \"2023-11-14\")";
        db.execSQL(INSERT_BILL);*/


        db.close();
    }

    //LoginValidate
    @SuppressLint("Range")
    public User checkUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        //boolean userExists = cursor.moveToFirst();

        User user = new User();

        if (cursor.moveToFirst()) {

            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
        }

        cursor.close();
        return user;


    }

}
