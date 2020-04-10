$(document).ready(function(){
    axios.get(`/api/news`)
        .then(function (response) {
            let list = document.getElementById("newsList");

            response.data.forEach(news => {
                const li = document.createElement("li");
                li.className = "news";

                const a = document.createElement("a");
                a.textContent = news.title;
                a.href = news.url;
                a.target = "_blank";

                const span = document.createElement("span");
                span.textContent = news.date;

                li.appendChild(a);
                li.appendChild(document.createElement("br"));
                li.appendChild(span);
                list.appendChild(li);
            });
        })
        .catch(function (error) {
            console.log(error);
        });
});