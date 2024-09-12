function confirmDelete(id) {
    var result = confirm("Are you sure you want to delete?");
    if (result) {
      // Send the delete request
      deleteStudent(id);
    }
  }
  
 

  function removeStudentCourse(button) {
    var courseItem = button.closest('.col-lg-3');
    var courseIdInput = courseItem.querySelector('.course-input[data-input-type="id"]');
    var studentCourseList = document.getElementById('student-course-list');
  
    // Get the index of the lecturer
    var index = Array.from(studentCourseList.children).indexOf(courseItem);
  
    // Remove the lecturer
    courseItem.parentNode.removeChild(courseItem);
  
    // Update the index values and remove corresponding lecturer
    courseIdInput.name = ''; // Remove the name attribute to exclude it from form submission
  
    // Update the index values and remove corresponding lecturer
    for (var i = index + 1; i < studentCourseList.children.length; i++) {
      var currenttem = studentCourseList.children[i];
      var currentItemIndex = i - 1;
  
      // Update the index values of the input fields
      var currentCourseIdInput = currentItem.querySelector('.course-input[data-input-type="id"]');
      var currentCourseNameInput = currentItem.querySelector('.course-input[data-input-type="name"]');
      currentCourseIdInput.name = 'courses[' + currentItemIndex + '].id';
      currentCourseNameInput.name = 'courses[' + currentItemIndex + '].course.name';
  
      // Update the corresponding lecturer object in the array
      courses[currentItemIndex] = lecturers[i];
    }
  
    // Remove the last item from the array
    courses.pop();
  }

  function deleteStudent(studentId) {
    var confirmed = confirm("Are you sure you want to delete this student?"+studentId);
    if (!confirmed) {
      return;
    }
    console.log(studentId);
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/admin/delete_student/" + studentId);
    xhr.onreadystatechange = function () {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 200) {
          location.reload();
          alert("Student deleted successfully!"); 
        } else {
          alert("Failed to delete student. Remove related courses and try again!");
        }
      }
    };
    xhr.send();
  }
  