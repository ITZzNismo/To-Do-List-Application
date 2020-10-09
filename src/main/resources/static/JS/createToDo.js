document.querySelector("form.newToDo").addEventListener("submit", function(stop)
{
    stop.preventDefault();
    let formElements = document.querySelector("form.NewToDo").elements;
    let name = formElements["name"].value;

    console.log(name)
    newToDo(name)
})