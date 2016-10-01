function api(version, $) {
    return {
        employees: {
            list: function () {
                return $.ajax({
                    url: "api/" + version + "/employees",
                    method: "GET",
                    dataType: 'json',
                    async: false
                }).responseJSON;
            },
            add: function (id, name) {
                $.ajax({
                    url: "api/" + version + "/employees/" + id,
                    method: "PUT",
                    dataType: 'json',
                    async: false,
                    contentType: "application/json",
                    data: JSON.stringify({name: name})
                });
            },
            edit: function (id, name) {
                $.ajax({
                    url: "api/" + version + "/employees/" + id,
                    method: "POST",
                    dataType: 'json',
                    async: false,
                    contentType: "application/json",
                    data: JSON.stringify({name: name})
                });
            },
            delete: function (id) {
                $.ajax({
                    url: "api/" + version + "/employees/" + id,
                    method: "DELETE",
                    dataType: 'json',
                    async: false
                });
            }
        },
        managers: {
            list: function () {
                return $.ajax({
                    url: "api/" + version + "/managers",
                    method: "GET",
                    dataType: 'json',
                    async: false
                }).responseJSON;
            },
            attach: function (id, employeeId) {
                $.ajax({
                    url: "api/" + version + "/managers/" + id + "/employees/" + employeeId,
                    method: "PUT",
                    dataType: 'json',
                    async: false
                });
            },
            detach: function (id, employeeId) {
                $.ajax({
                    url: "api/" + version + "/managers/" + id + "/employees/" + employeeId,
                    method: "DELETE",
                    dataType: 'json',
                    async: false
                });
            }
        }
    }
}
