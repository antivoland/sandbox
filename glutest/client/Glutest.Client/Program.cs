using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.IO;
using System.Runtime.Serialization.Json;

namespace Glutest.Client
{
    class Program
    {
        private static string host = "127.0.0.1";
        private static int port = 8181;
        private static string ping = "/ping";

        static void Main(string[] args)
        {
            Console.Out.WriteLine("Hello, World!");

            Transport.BusinessReq<object> req = new Transport.BusinessReq<object>();
            req.userId = "c#client";

            string strReq = ToJson(req);

            string path = "http://" + host + ":" + port + ping;
            Transport.BusinessResp<object> resp = MakeRequest(path, strReq);




            Console.In.Read();
        }

        public static T Deserialize<T>(string jsonString)
        {

            using (MemoryStream ms = new MemoryStream(Encoding.Unicode.GetBytes(jsonString)))
            {
                DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(T));
                return (T)serializer.ReadObject(ms);
            }

        }

        public static Transport.BusinessResp<object> MakeRequest(string requestUrl, string req)
        {
            try
            {

                HttpWebRequest r = WebRequest.Create(requestUrl) as HttpWebRequest;
                r.Method = "POST";
                UTF8Encoding encoding = new UTF8Encoding();

                byte[] byte1 = encoding.GetBytes(req);

                // r.ContentLength = encoding.GetByteCount(req);
                r.ContentLength = byte1.Length;
                r.Credentials = CredentialCache.DefaultCredentials;
                r.Accept = "application/json";
                r.ContentType = "application/json";


                Stream newStream = r.GetRequestStream();

                newStream.Write(byte1, 0, byte1.Length);

                using (HttpWebResponse response = r.GetResponse() as HttpWebResponse)
                {
                    if (response.StatusCode != HttpStatusCode.OK)
                        throw new Exception(String.Format(
                        "Server error (HTTP {0}: {1}).",
                        response.StatusCode,
                        response.StatusDescription));
                    DataContractJsonSerializer jsonSerializer = new DataContractJsonSerializer(typeof(Response));
                    object objResponse = jsonSerializer.ReadObject(response.GetResponseStream());
                    Transport.BusinessResp<object> jsonResponse
                    = objResponse as Transport.BusinessResp<object>;
                    return jsonResponse;
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return null;
            }
        }

        private static string ToJson<T>(T data)
        {
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(T));

            using (MemoryStream ms = new MemoryStream())
            {
                serializer.WriteObject(ms, data);
                return Encoding.Default.GetString(ms.ToArray());
            }
        }
    }
}
