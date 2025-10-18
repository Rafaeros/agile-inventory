function removeMaterial(button) {
    const div = button.closest('.material-div');
    div.querySelectorAll('input').forEach(input => input.disabled = true);
    div.remove();
    document.querySelectorAll('.material-div').forEach((div, index) => {
        div.querySelectorAll('input').forEach(input => {
            input.name = input.name.replace(/\[\d+\]/, `[${index}]`);
        });
    });
}