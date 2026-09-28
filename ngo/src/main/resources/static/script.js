const API = "/api";

function show(text, ok) {
    const m = document.getElementById("msg");
    m.textContent = text;
    m.className = ok ? "ok" : "err";
}

async function send(url, method, body) {
    const res = await fetch(API + url, {
        method: method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.message || "Something went wrong.");
    return data;
}

async function loadEvents() {
    const events = await (await fetch(API + "/events")).json();
    document.getElementById("events").innerHTML = events.map(e =>
        `<tr><td>${e.id}</td><td>${e.name}</td><td>${e.date}</td><td>${e.location}</td><td>${e.capacity}</td></tr>`
    ).join("");
    const options = events.map(e => `<option value="${e.id}">${e.name}</option>`).join("");
    document.getElementById("vevent").innerHTML = options;
    document.getElementById("aevent").innerHTML = options;
    loadVolunteers();
}

async function addEvent() {
    try {
        await send("/events", "POST", {
            name: document.getElementById("ename").value,
            date: document.getElementById("edate").value || null,
            location: document.getElementById("eloc").value,
            capacity: Number(document.getElementById("ecap").value)
        });
        show("Event added.", true);
        loadEvents();
    } catch (e) { show(e.message, false); }
}

async function signup() {
    try {
        await send("/volunteers/signup", "POST", {
            name: document.getElementById("vname").value,
            email: document.getElementById("vemail").value,
            eventId: Number(document.getElementById("vevent").value) || null
        });
        show("Volunteer registered.", true);
        loadVolunteers();
    } catch (e) { show(e.message, false); }
}

async function loadVolunteers() {
    const id = document.getElementById("aevent").value;
    if (!id) { document.getElementById("volunteers").innerHTML = ""; return; }
    const list = await (await fetch(API + "/volunteers/event/" + id)).json();
    document.getElementById("volunteers").innerHTML = list.map(v => {
        const status = v.attended === null ? "Not marked" : (v.attended ? "Present" : "Absent");
        return `<tr><td>${v.name}</td><td>${v.email}</td><td>${status}</td>
            <td><input id="h${v.id}" type="number" step="0.5" min="0" value="${v.hours}" style="width:60px"></td>
            <td><button onclick="mark(${v.id}, true)">Present</button>
                <button class="red" onclick="mark(${v.id}, false)">Absent</button></td></tr>`;
    }).join("");
}

async function mark(id, present) {
    try {
        await send("/volunteers/" + id + "/attendance", "PUT", {
            attended: present,
            hours: Number(document.getElementById("h" + id).value) || 0
        });
        show("Attendance saved.", true);
        loadVolunteers();
    } catch (e) { show(e.message, false); }
}

loadEvents();
