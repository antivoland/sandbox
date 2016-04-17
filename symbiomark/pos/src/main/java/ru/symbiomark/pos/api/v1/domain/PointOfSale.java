package ru.symbiomark.pos.api.v1.domain;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/*
  {
      "id":INT,
      "latitude":FLOAT,
      "longitude":FLOAT,
      "companyIds":[INT], - идентификаторы компаний (например, "Ашан", "Перекрёсток")
      "address":STRING,
      "workTime":{ - время работы торговой точки
          "Mon":[ - массив времени работы в данный день недели; если пустой - круглосуточно, если день отсутствует - выходной
              ["09:00","13:00"],
              ["14:00","19:00"],
              ...
          ],
          "Fri":[
              ["10:00","13:30"],
              ["15:00","17:30"],
              ...
          ],
          ...
      },
      "contacts":[ - контакты торговой точки
          {
              "type":STRING, - URL, PHONE, E_MAIL
              "value":STRING,
              "name":STRING|NULL
          },
          ...
      ],
  }
 */
public class PointOfSale {
    public int id;
    public float latitude;
    public float longitude;
    public List<Integer> companyIds;
    public String address;
    public Map<DayOfWeek, List<LocalTime>> workTime;
    public List<Contact> contacts;
}
