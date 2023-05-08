import mysql.connector
import csv
import hashlib
import password

def select_table(db, table_name):
    cursor = db.cursor()
    cursor.execute(("SELECT * FROM " + table_name))

    results = cursor.fetchall()

    return [x for x in results]

def update_password(db, username, raw_password):
    md5_password = hashlib.md5(raw_password.encode()).hexdigest().upper()

    cursor = db.cursor()
    sql = "UPDATE jhi_user SET password_hash= %s WHERE login = %s"
    cursor.execute(sql, (md5_password, username))
    db.commit()

if __name__ == "__main__":
    db = mysql.connector.connect(
      host="localhost",
      user="hailiang194",
      password="Capstone@2022",
      database="capstone_db"
    )

    users = select_table(db, "jhi_user")
    
    with open('new_account.csv', mode='w') as account_file:
        account_writer = csv.writer(account_file)

        for user in users:
            username = user[1]
            print("Generate for " + username + "...")
            pwd = "123456"
            print("New password: " + pwd)
            print("Update MySQL Database...")
            update_password(db, username, pwd)
            print("Updated")
            print("Writing to csv...")
            account_writer.writerow([username, pwd])
            print("Wrote")
            if not username == "admin":
                break
