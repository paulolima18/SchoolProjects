from pymongo import MongoClient
from random import randint
import urllib.request as request
import json

BASE_URL = 'http://nosql.hpeixoto.me/api/sensor/'


def load_blood(db):
  for i in range(1,6):
    current_sensor = 4000+i
    url = BASE_URL + str(current_sensor)
    with request.urlopen(url) as response:
      source = response.read()
      data = json.loads(source)
      result=db.blood.insert_one(data)
      print('Created {0} of 5 as {1}'.format(i, result.inserted_id))

  print('finished creating 5 blood cardiac sensors')

def load_cardiac(db):
  for i in range(1,6):
    current_sensor = 3000+i
    url = BASE_URL + str(current_sensor)
    with request.urlopen(url) as response:
      source = response.read()
      data = json.loads(source)
      result=db.cardiac.insert_one(data)
      print('Created {0} of 5 as {1}'.format(i, result.inserted_id))

  print('finished creating 5 healthcare cardiac sensors')

def main():
  try:
    client = MongoClient(port=27017)
    db     = client.healthcare

    load_blood(db)
    load_cardiac(db)
  except Exception as identifier:
    print(identifier)
    exit(99)


if __name__ == '__main__':
    main()