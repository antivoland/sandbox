package ru.symbiomark.pos.api.v1.domain;

/*
  {
      "type":STRING, - URL, PHONE, E_MAIL
      "value":STRING,
      "name":STRING|NULL
  }
 */
public class Contact {
    public enum Type {URL, PHONE, E_MAIL}

    public Type type;
    public String value;
    public String name;
}
