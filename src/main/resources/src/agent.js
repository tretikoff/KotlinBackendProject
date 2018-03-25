import superagentPromise from 'superagent-promise';
import _superagent from 'superagent';
import moment from 'moment';

const superagent = superagentPromise(_superagent, global.Promise);

const API_ROOT = '/api';

const responseBody = res => res.body;

let token = null;
const tokenPlugin = req => {
    if (token) {
        req.set('authorization', `Token ${token}`);
    }
};

const requests = {
    del: url =>
        superagent.del(`${API_ROOT}${url}`).use(tokenPlugin).then(responseBody),
    get: url =>
        superagent.get(`${API_ROOT}${url}`).use(tokenPlugin).then(responseBody),
    put: (url, body) =>
        superagent.put(`${API_ROOT}${url}`, body).use(tokenPlugin).then(responseBody),
    post: (url, body) =>
        superagent.post(`${API_ROOT}${url}`, body).use(tokenPlugin).then(responseBody)
};

const Auth = {
    current: () =>
        requests.get('/user'),
    login: (email, password) =>
        requests.post('/users/login', {user: {email, password}}),
    register: (username, email, password) =>
        requests.post('/users', {user: {username, email, password}}),
    save: user =>
        requests.put('/user', {user})
};

const Profile = {
    follow: username =>
        requests.post(`/profiles/${username}/follow`),
    get: username =>
        requests.get(`/profiles/${username}`),
    unfollow: username =>
        requests.del(`/profiles/${username}/follow`)
};


const Groups = {
    userGroups: async user => {
        let answer = (await requests.get('/groups', user.id)).groups;
        console.log(">>>>>>>>>>>>>>>", answer);
        return answer;
    },

    create: async group => {
        group.id = 1;
        let answer = (await requests.post('/groups', {group}));
        console.log(">>>>>>>>>>>>>>>", answer);
        return answer;
    },

    del: async id => {
        let answer = (await requests.del(`/groups/${id}`));
        console.log(">>>>>>>>>>>>>>>", answer);
        return answer;
    },

    find: async partialName => {
        let answer = (await requests.get(`/groups/${partialName}`));
        console.log(">>>>>>>>>>>>>>>", answer);
        return answer.groups;
    }
};

let news = [
    {
        id: '1',
        groupId: '1',
        header: "Something big happened",
        info: 'Francis Ford Coppolas legendary continuation and sequel to his landmark 1972 film, The_Godfather parallels ' +
        'the young Vito Corleone\'s rise with his son Michael\'s spiritual fall, deepening The_Godfathers depiction of the' +
        ' dark side of the American dream. In the early 1900s, the child Vito flees his Sicilian village for America after ' +
        'the local Mafia kills his family. Vito struggles to make a living, legally or illegally, for his wife and growing ' +
        'brood in Little Italy, killing the local Black Hand Fanucci after he demands his customary cut of the tyro\'s business.' +
        ' With Fanucci gone, Vito\'s communal stature grows.'
    }
];

const News = {
    create: async news => {
        news.id = 0;
        let answer = (await requests.post('/news', {news: news})).news;
        console.log(">>>>>>>>>>>>>>>", answer);
        return answer;
    },
    forGroup: async id => {
        let answer = await requests.get('/news', id);
        console.log(">>>>>>>>>>>>>>>", answer);
        return answer;
    }
};

const Events = {
    userEvents: async () => {
        let answer = (await requests.get('/events')).events;
        console.log(">>>>>>>>>>>>>>>", answer);
        return answer;
    },
    create: async event => {
        event.id = 0;
        let start = moment(event.start, "llll").format("YYYY-MM-DD");
        let end = moment(event.end, "llll").format("YYYY-MM-DD");
        event.start = start;
        event.end = end;
        let answer = await requests.post('/events', {event: event});
        console.log(">>>>>", answer);
        return answer;
    },
    del: async id => {
        let answer = await requests.del(`/events/${id}`);
        console.log(">>>>>", answer);
        return answer;
    },
    edit: async (event) => {
        let answer = await requests.put(`/events`, {event: event});
        console.log(">>>>>", answer);
        return answer;
    }
};

const Notes = {
    userNotes: async user => {
        let answer = (await requests.get('/notes')).notes;
        console.log(">>>>>>>>>>>>>>>", answer);
        return answer;
    },
    create: async note => {
        note.id = 0;
        let answer = (await requests.post('/notes', {note}));
        console.log(">>>>>>>>>>>>>>>", answer);
        return answer;
    },

    del: async id => {
        let answer = await requests.del(`/notes/${id}`);
        console.log(">>>>>>>>>>>>>>>", answer);
        return answer;
    }
};

export default {
    Auth,
    Profile,
    Groups,
    News,
    Events,
    Notes,
    setToken: _token => {
        token = _token;
    }
};