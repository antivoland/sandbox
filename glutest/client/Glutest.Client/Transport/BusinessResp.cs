using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;

namespace Glutest.Client.Transport
{
    [DataContract]
    class BusinessResp<T> where T : class
    {
        [DataMember(Name = "code")]
        public int Code { get; set; }
        [DataMember(Name = "message")]
        public string Message { get; set; }
        [DataMember(Name = "payload")]
        public T Payload { get; set; }
    }
}
