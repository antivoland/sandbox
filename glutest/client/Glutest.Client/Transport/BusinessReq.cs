using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;

namespace Glutest.Client.Transport
{
    [DataContract]
    class BusinessReq<T> where T : class
    {
        [DataMember(Name = "userId")]
        public string userId { get; set; }
        [DataMember(Name = "time")]
        public long time { get; set; }
        [DataMember(Name = "signature")]
        public string signature { get; set; }
        [DataMember(Name = "payload")]
        public T payload { get; set; }
    }
}
