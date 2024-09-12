
function removeCourse(button) {
  var courseItem = button.closest('.col-lg-3');
  var courseIdInput = courseItem.querySelector('.course-input[data-input-type="id"]');
  var courseList = document.getElementById('course-list');
  var index = Array.from(courseList.children).indexOf(courseItem);

  // Remove the course item
  courseItem.parentNode.removeChild(courseItem);
  courseIdInput.name = ''; 

  for (var i = index + 1; i < courseList.children.length; i++) {
    var currentItem = courseList.children[i];
    var currentItemIndex = i - 1;

    // Update the index values of the input fields
    var currentCourseIdInput = currentItem.querySelector('.course-input[data-input-type="id"]');
    var currentCourseNameInput = currentItem.querySelector('.course-input[data-input-type="name"]');
    currentCourseIdInput.name = 'courses[' + currentItemIndex + '].id';
    currentCourseNameInput.name = 'courses[' + currentItemIndex + '].name';

    // Update the corresponding course object in the array
    courses[currentItemIndex] = courses[i];
  }

  courses.pop();
}



function addCourse() {
  var selectElement = document.getElementById('selectedCourse');
  var selectedOption = selectElement.options[selectElement.selectedIndex];
  var selectedCourseId = selectedOption.getAttribute('data-course-id');
  var selectedCourseName = selectedOption.textContent;

  if (selectedCourseId) {
      // Check if the course already exists in the course list
      var existingCourses = document.querySelectorAll('.course-item .course-input');
      var isCourseExists = Array.from(existingCourses).some(function (courseInput) {
          return courseInput.value === selectedCourseName;
      });

      if (isCourseExists) {
          selectedOption.disabled = true;
          selectElement.selectedIndex = 0; 
          return;
      }

      // Create the new course item
      var newCourseItem = document.createElement('div');
      newCourseItem.classList.add('col-lg-3', 'col-md-4', 'col-sm-6', 'course-item');

      var inputGroup = document.createElement('div');
      inputGroup.classList.add('input-group');

      // Create hidden input field for course ID
      var courseIdInput = document.createElement('input');
      courseIdInput.type = 'hidden';
      courseIdInput.name = 'courses[' + document.querySelectorAll('.course-item').length + '].id';
      courseIdInput.value = selectedCourseId;

      // Create input field for course name
      var courseNameInput = document.createElement('input');
      courseNameInput.type = 'text';
      courseNameInput.name = 'courses[' + document.querySelectorAll('.course-item').length + '].name';
      courseNameInput.value = selectedCourseName;
      courseNameInput.classList.add('form-control', 'course-input');
      courseNameInput.readOnly = true;

      // Append the input fields to the input group
      inputGroup.appendChild(courseIdInput);
      inputGroup.appendChild(courseNameInput);

      var inputGroupAppend = document.createElement('div');
      inputGroupAppend.classList.add('input-group-append');

      var removeButton = document.createElement('button');
      removeButton.type = 'button';
      removeButton.classList.add('btn', 'btn-sm', 'btn-danger', 'remove-course');
      removeButton.textContent = 'Remove';
      removeButton.addEventListener('click', function () {
          removeCourse(this);
      });

      // Construct the course item
      inputGroupAppend.appendChild(removeButton);
      inputGroup.appendChild(inputGroupAppend);
      newCourseItem.appendChild(inputGroup);

      // Add the new course item to the course list
      var courseList = document.getElementById('course-list');
      courseList.appendChild(newCourseItem);

      // Clear the selected course in the dropdown
      selectElement.selectedIndex = 0;
  }
}



function addLecturer() {
  var selectElement = document.getElementById('selectedLecturer');
  var selectedOption = selectElement.options[selectElement.selectedIndex];
  var selectedLecturerId = selectedOption.getAttribute('data-lecturer-id');
  var selectedLecturerName = selectedOption.textContent;

  if (selectedLecturerId) {
      // Check if the lecturer already exists in the lecturer list
      var existingLecturers = document.querySelectorAll('.lecturer-item .lecturer-input');
      var isLecturerExists = Array.from(existingLecturers).some(function (lecturerInput) {
          return lecturerInput.value === selectedLecturerName;
      });

      if (isLecturerExists) {
          // Disable the selected option if the lecturer already exists
          selectedOption.disabled = true;
          selectElement.selectedIndex = 0; // Reset the selection to the default option
          return;
      }

      // Create the new lecturer
      var newLecturerItem = document.createElement('div');
      newLecturerItem.classList.add('col-lg-3', 'col-md-4', 'col-sm-6', 'lecturer-item');

      var inputGroup = document.createElement('div');
      inputGroup.classList.add('input-group');

      // Create hidden input field for lecturer Id
      var lecturerIdInput = document.createElement('input');
      lecturerIdInput.type = 'hidden';
      lecturerIdInput.name = 'lecturers[' + document.querySelectorAll('.lecturer-item').length + '].id';
      lecturerIdInput.value = selectedLecturerId;

      // Create input field for lecturer name
      var lecturerNameInput = document.createElement('input');
      lecturerNameInput.type = 'text';
      lecturerNameInput.name = 'lecturers[' + document.querySelectorAll('.lecturer-item').length + '].name';
      lecturerNameInput.value = selectedLecturerName;
      lecturerNameInput.classList.add('form-control', 'lecturer-input');
      lecturerNameInput.readOnly = true;

      inputGroup.appendChild(lecturerIdInput);
      inputGroup.appendChild(lecturerNameInput);

      var inputGroupAppend = document.createElement('div');
      inputGroupAppend.classList.add('input-group-append');

      var removeButton = document.createElement('button');
      removeButton.type = 'button';
      removeButton.classList.add('btn', 'btn-sm', 'btn-danger', 'remove-lecturer');
      removeButton.textContent = 'Remove';
      removeButton.addEventListener('click', function () {
          removeCourse(this);
      });

      // Create the lecturer
      inputGroupAppend.appendChild(removeButton);
      inputGroup.appendChild(inputGroupAppend);
      newLecturerItem.appendChild(inputGroup);

      // Add the new lecturer to the course list
      var lecturerList = document.getElementById('lecturer-list');
      lecturerList.appendChild(newLecturerItem);

      selectElement.selectedIndex = 0;
  }
}


function removeLecturer(button) {
  var lecturerItem = button.closest('.col-lg-3');
  var lecturerIdInput = lecturerItem.querySelector('.lecturer-input[data-input-type="id"]');
  var lecturerList = document.getElementById('lecturer-list');

  // Get the index of the lecturer
  var index = Array.from(lecturerList.children).indexOf(lecturerItem);

  // Remove the lecturer
  lecturerItem.parentNode.removeChild(lecturerItem);
  lecturerIdInput.name = ''; 

  // Update the index values and remove corresponding lecturer
  for (var i = index + 1; i < lecturerList.children.length; i++) {
    var currentItem = lecturerList.children[i];
    var currentItemIndex = i - 1;

    // Update the index values of the input fields
    var currentLecturerIdInput = currentItem.querySelector('.lecturer-input[data-input-type="id"]');
    var currentLecturerNameInput = currentItem.querySelector('.lecturer-input[data-input-type="name"]');
    currentLecturerIdInput.name = 'lecturers[' + currentItemIndex + '].id';
    currentLecturerNameInput.name = 'lecturers[' + currentItemIndex + '].name';

    // Update the corresponding lecturer object in the array
    lecturers[currentItemIndex] = lecturers[i];
  }

  lecturers.pop();
}


function deleteAdmin(adminId) {
  var confirmed = confirm("Are you sure you want to delete this admin? "+ adminId);
  if (!confirmed) {
    return;
  }
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "http://localhost:8080/admin/delete_admin/" + adminId);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === XMLHttpRequest.DONE) {
      if (xhr.status === 200) {
        location.reload();
        alert("Admin deleted successfully!"); 
      } else {
        alert("Failed to delete admin");
      }
    }
  };
  xhr.send();
}

function goBack(){
  history.go(-1);
}