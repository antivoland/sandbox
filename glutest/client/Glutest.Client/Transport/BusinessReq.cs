using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;
using System.Net;
using System.IO;
using System.Runtime.Serialization.Json;

namespace Glutest.Client.Transport
{
    [DataContract]
    class BusinessReq<T> where T : class
    {
        [DataMember(Name = "userId")]
        public string UserId { get; set; }
        [DataMember(Name = "time")]
        public long Time { get; set; }
        [DataMember(Name = "signature")]
        public string Signature { get; set; }
        [DataMember(Name = "payload")]
        public T Payload { get; set; }

        public BusinessResp<P> Execute<P>(string url) where P : class
        {
            try
            {
                HttpWebRequest httpReq = WebRequest.Create(url) as HttpWebRequest;
                httpReq.Method = "POST";

                UTF8Encoding encoding = new UTF8Encoding();
                string reqStr = Helper.ToJsonString<BusinessReq<T>>(this);
                byte[] reqBytes = encoding.GetBytes(reqStr);

                httpReq.ContentLength = reqBytes.Length;
                httpReq.Credentials = CredentialCache.DefaultCredentials;
                httpReq.Accept = "application/json";
                httpReq.ContentType = "application/json";

                httpReq.GetRequestStream().Write(reqBytes, 0, reqBytes.Length);

                using (HttpWebResponse httpResp = httpReq.GetResponse() as HttpWebResponse)
                {
                    if (httpResp.StatusCode != HttpStatusCode.OK)
                    {
                        throw new Exception(String.Format("Server error (HTTP {0}: {1}).", httpResp.StatusCode, httpResp.StatusDescription));
                    }
                    DataContractJsonSerializer jsonSerializer = new DataContractJsonSerializer(typeof(BusinessResp<P>));
                    return jsonSerializer.ReadObject(httpResp.GetResponseStream()) as BusinessResp<P>;
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return null;
            }
        }
    }
}
