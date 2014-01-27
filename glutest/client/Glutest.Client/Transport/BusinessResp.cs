using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Glutest.Client.Transport
{
    class BusinessResp<T> where T : class
    {
        public int code { get; set; }
        public string message { get; set; }
        public T payload { get; set; }
    }
}
