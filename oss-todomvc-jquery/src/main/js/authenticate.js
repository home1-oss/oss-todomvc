var INDEX_PAGE = "index.html";
var LOGIN_URL = "/api/login";

function tryRedirect() {
  let username = localStorage.getItem("name");
  let token = localStorage.getItem("token");
  if (username != null && username.trim() != "" && token != null && token.trim() != "") {
    window.location.href = "index.html";
  }
}

function check(form) {

  var requestBody = {
    username: form.username.value,
    password: form.password.value
  }

  $.ajax({
    url: LOGIN_URL,
    type: "POST",
    async: false,
    contentType: "application/x-www-form-urlencoded;charset=UTF-8",
    dataType: "json",
    data: requestBody,
    success: function (data, status, xhr) {
      token = xhr.getResponseHeader("X-Auth-Token");
      localStorage.setItem("token", token);

      localStorage.setItem("name", data.name);
      window.location.href = "index.html";
    },
    error: function (xhr, status, error) {
      alert(xhr.responseText);
    },
  });
}

tryRedirect();
