from datetime import datetime
from elasticsearch import Elasticsearch
import pprint
import urllib.request as request
import requests
import json
import time
import threading
import time

BASE_URL = 'http://nosql.hpeixoto.me/api/sensor_elastic/'
CARDIAC = 'cardiac'
BLOOD = 'blood'
V_3 = [id for id in range(3001,3006)]
V_4 = [id for id in range(4001,4006)]

exitFlag = 0

class sensor_thread (threading.Thread):
   def __init__(self, sensor_id, sensor_type):
      threading.Thread.__init__(self)
      self.sensor_id = sensor_id
      self.sensor_type = sensor_type
   def run(self):
      print("Starting/Thread-" + str(self.sensor_id))
      main(self.sensor_id, self.sensor_type)
      print("Exiting/Thread-" + str(self.sensor_id))

def get_json(sensor_id):
  try:
    url = BASE_URL + str(sensor_id)
    with request.urlopen(url) as response:
      source = response.read()
      data = json.loads(source)
      return data
  except Exception as e:
    print(e)

def send_post(es, sensor_id, sensor_type):
  data = get_json(sensor_id)
  # header = {'Content-type': 'application/json'}
  # url = 'http://localhost:9200/'+sensor_type+'/_doc/?pretty'
  req = es.index(index=sensor_type, id=sensor_id, body=data)

  #req = requests.post(url, data=data, headers=header)
  print("Thread-" + str(sensor_id) + ":" + str(req))


#3001-3005/4001-4005
def main(sensor_id, sensor_type):
  try:
    duration = 5*60
    es = Elasticsearch()
    while duration > 0:
      send_post(es, sensor_id, sensor_type)
      time.sleep(10)
      duration -= 10
    print('Bye, left thread:'+str(sensor_id))
  except Exception as e:
    print(e)


# {'acknowledged': True, 'shards_acknowledged': True, 'index': 'my-index'}
# pprint.pprint(es.get(index="inspections", id="KWhLAHYB7bOE5cNS3GDO"))

if __name__ == '__main__':
  try:
    es = Elasticsearch()
    es.indices.create(index=CARDIAC, ignore=400)
    es.indices.create(index=BLOOD, ignore=400)
    threads = []
    for sensor_id in V_3:
      threads.append(sensor_thread(sensor_id, CARDIAC))
    for sensor_id in V_4:
      threads.append(sensor_thread(sensor_id, BLOOD))

    for thread in threads:
      thread.start()
  except Exception as e:
    print(e)