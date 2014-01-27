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
        private static string HOST = "127.0.0.1";
        private static int PORT = 8181;

        static void Main(string[] args)
        {
            Console.Out.WriteLine("Type \"ping\" to ping or \"exit\" to exit.");
            string cmd;
            do
            {
                cmd = Console.In.ReadLine();
                if ("ping".Equals(cmd))
                {
                    ping();
                }
            } while (!"exit".Equals(cmd));
        }

        private static void ping()
        {
            Transport.BusinessReq<object> req = new Transport.BusinessReq<object>();
            req.UserId = "c#client";
            Console.Out.WriteLine("REQ " + Transport.Helper.ToJsonString<Transport.BusinessReq<object>>(req));

            string url = "http://" + HOST + ":" + PORT + "/ping";
            Transport.BusinessResp<object> resp = req.Execute<object>(url);
            Console.Out.WriteLine("RESP " + Transport.Helper.ToJsonString<Transport.BusinessResp<object>>(resp));
        }
    }
}
