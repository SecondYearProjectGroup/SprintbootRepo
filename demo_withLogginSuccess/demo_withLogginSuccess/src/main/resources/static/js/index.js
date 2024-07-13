function Alert() {
    window.alert("You enrolled successfully.");
}

// Approve/ Reject Students in Registered Students
document.addEventListener('DOMContentLoaded', function() {
    const statusCell = document.getElementById('abc');
    const rightButton = document.getElementById('approve-button');
    const wrongButton = document.getElementById('reject-button');

    rightButton.addEventListener('click', function() {
        statusCell.innerHTML = '<strong>Approved</strong>';
        statusCell.style.color = 'green'; // Set color to green for "Approved"
    });

    wrongButton.addEventListener('click', function() {
        statusCell.innerHTML = '<strong>Rejected</strong>';
        statusCell.style.color = 'red'; // Set color to red for "Rejected"
    });
});

document.addEventListener('DOMContentLoaded', function() {
    var notificationButton = document.getElementById('notificationButton');
    var notificationPanel = document.getElementById('notificationPanel');

    notificationButton.addEventListener('click', function() {

        event.stopPropagation();

        if (notificationPanel.style.display === 'none' || notificationPanel.style.display === '') {
            notificationPanel.style.display = 'block';
        } else {
            notificationPanel.style.display = 'none';
        }
    });

    // Hide the notification panel if clicked outside
    document.addEventListener('click', function(event) {
        var isClickInside = notificationPanel.contains(event.target) || notificationButton.contains(event.target);

        if (!isClickInside) {
            notificationPanel.style.display = 'none';
        }
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const sidebar = document.getElementById('rightSidebar');
    const mainContent = document.getElementById('mainContent');
    const hideSidebarBtn = document.getElementById('hideSidebarBtn');
    const showSidebarBtn = document.getElementById('showSidebarBtn');

    const sidebarState = localStorage.getItem('sidebarState');

    if (sidebarState === 'hidden') {
        sidebar.classList.add('hidden');
        mainContent.classList.add('expanded');
        hideSidebarBtn.style.display = 'none';
        showSidebarBtn.style.display = 'block';
    }

    hideSidebarBtn.addEventListener('click', () => {
        sidebar.classList.add('hidden');
        mainContent.classList.add('expanded');
        hideSidebarBtn.style.display = 'none';
        showSidebarBtn.style.display = 'block';
        localStorage.setItem('sidebarState', 'hidden');
    });

    showSidebarBtn.addEventListener('click', () => {
        sidebar.classList.remove('hidden');
        mainContent.classList.remove('expanded');
        hideSidebarBtn.style.display = 'block';
        showSidebarBtn.style.display = 'none';
        localStorage.setItem('sidebarState', 'visible');
    });
});
