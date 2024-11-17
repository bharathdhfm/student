const apiUrl = 'http://localhost:8080/students'; // Ensure this URL matches your backend

document.getElementById('studentForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const id = document.getElementById('studentId').value;
    const name = document.getElementById('studentName').value;
    const gender = document.getElementById('gender').value;

    if (id) {
        updateStudent(id, { name, gender });
    } else {
        addStudent({ name, gender });
    }
});

// Load students on page load
window.onload = function() {
    fetchStudents();
};

// Fetch students from the server
function fetchStudents() {
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            populateStudentTable(data);
        })
        .catch(error => console.error('Error fetching students:', error));
}

// Populate the student table
function populateStudentTable(students) {
    const studentBody = document.getElementById('studentBody');
    studentBody.innerHTML = ''; // Clear existing table rows
    students.forEach(student => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${student.name}</td>
            <td>${student.gender}</td>
            <td>
                <button onclick="editStudent(${student.id}, '${student.name}', '${student.gender}')">Edit</button>
                <button onclick="deleteStudent(${student.id})">Delete</button>
            </td>
        `;
        studentBody.appendChild(row);
    });
}

// Add a new student
function addStudent(student) {
    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(student),
    })
    .then(response => response.json())
    .then(() => {
        fetchStudents(); // Refresh the student list
        clearForm();
    })
    .catch(error => console.error('Error adding student:', error));
}

// Update a student
function updateStudent(id, student) {
    fetch(`${apiUrl}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(student),
    })
    .then(response => response.json())
    .then(() => {
        fetchStudents(); // Refresh the student list
        clearForm();
    })
    .catch(error => console.error('Error updating student:', error));
}

// Delete a student
function deleteStudent(id) {
    fetch(`${apiUrl}/${id}`, {
        method: 'DELETE',
    })
    .then(() => {
        fetchStudents(); // Refresh the student list
    })
    .catch(error => console.error('Error deleting student:', error));
}

// Edit a student
function editStudent(id, name, gender) {
    document.getElementById('studentId').value = id;
    document.getElementById('studentName').value = name;
    document.getElementById('gender').value = gender;
}

// Clear the form fields
function clearForm() {
    document.getElementById('studentId').value = '';
    document.getElementById('studentName').value = '';
    document.getElementById('gender').value = '';
}

// Search students
function searchStudents() {
    const query = document.getElementById('search').value.toLowerCase();
    const rows = document.querySelectorAll('#studentBody tr');

    rows.forEach(row => {
        const name = row.cells[0].textContent.toLowerCase();
        const gender = row.cells[1].textContent.toLowerCase();
        if (name.includes(query) || gender.includes(query)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}
