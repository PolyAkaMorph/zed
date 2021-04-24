import random
import string
import time
from russian_names import RussianNames


def randstr():
    return ''.join(random.choice(string.ascii_uppercase + string.ascii_lowercase + string.digits) for _ in range(16))


File_object = open("data.csv", "w", encoding="UTF-8")
start_time = time.time()
loops=1000
for x in range(loops):
    batchFemale = RussianNames(count=500, output_type='dict', gender=0).get_batch()
    batchMale = RussianNames(count=500, output_type='dict', gender=1).get_batch()
    for element in batchFemale:
        File_object.write(randstr() + ",")
        File_object.write(randstr() + ",")
        File_object.write(element['name'] + ",")
        File_object.write(element['surname'] + ",")
        File_object.write(str(random.randint(12, 90)) + ",")
        File_object.write("Female" + ",")
        File_object.write(randstr() + ",")
        File_object.write(randstr())
        File_object.write("\n")
    for element in batchMale:
        File_object.write(randstr() + ",")
        File_object.write(randstr() + ",")
        File_object.write(element['name'] + ",")
        File_object.write(element['surname'] + ",")
        File_object.write(str(random.randint(12, 90)) + ",")
        File_object.write("Male" + ",")
        File_object.write(randstr() + ",")
        File_object.write(randstr())
        File_object.write("\n")
    percent=(x * 100) / loops
    print("Working... " + str(percent) + "%")
File_object.close()
print("--- %s seconds ---" % (time.time() - start_time))


#CREATE TABLE `social`.`user` (
# `user_id` INT NOT NULL AUTO_INCREMENT,
# `login` VARCHAR(255) NOT NULL UNIQUE,
# `password_hash` VARCHAR(255) NOT NULL,
# `registration_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
# `last_login_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
# `role` VARCHAR(32) NOT NULL DEFAULT 'USER',
# `name` VARCHAR(225) NULL,
# `surname` VARCHAR(225) NULL,
# `age` VARCHAR(45) NULL,
# `sex` VARCHAR(45) NULL,
# `interests` VARCHAR(225) NULL,
# `city` VARCHAR(45) NULL,
# PRIMARY KEY (`user_id`));