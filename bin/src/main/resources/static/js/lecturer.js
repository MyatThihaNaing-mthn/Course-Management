function deleteLecturer(lecturerId) {
  var confirmed = confirm("Are you sure you want to delete this lecturer?"+lecturerId);
  if (!confirmed) {
    return;
  }
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "http://localhost:8080/admin/lecturer/delete/" + lecturerId);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === XMLHttpRequest.DONE) {
      if (xhr.status === 200) {
        location.reload();
        alert("Lecturer deleted successfully!"); 
      } else {
        alert("Failed to delete lecturer.");
      }
    }
  };
  xhr.send();
}
