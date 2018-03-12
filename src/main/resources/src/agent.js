import superagentPromise from 'superagent-promise';
import _superagent from 'superagent';

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

// const Tags = {
//   getAll: () => requests.get('/tags')
// };
//
// const limit = (count, p) => `limit=${count}&offset=${p ? p * count : 0}`;
// const omitSlug = article => Object.assign({}, article, {slug: undefined})
// const Articles = {
//   all: page =>
//     requests.get(`/articles?${limit(10, page)}`),
//   byAuthor: (author, page) =>
//     requests.get(`/articles?author=${encode(author)}&${limit(5, page)}`),
//   byTag: (tag, page) =>
//     requests.get(`/articles?tag=${encode(tag)}&${limit(10, page)}`),
//   del: slug =>
//     requests.del(`/articles/${slug}`),
//   favorite: slug =>
//     requests.post(`/articles/${slug}/favorite`),
//   favoritedBy: (author, page) =>
//     requests.get(`/articles?favorited=${encode(author)}&${limit(5, page)}`),
//   feed: () =>
//     requests.get('/articles/feed?limit=10&offset=0'),
//   get: slug =>
//     requests.get(`/articles/${slug}`),
//   unfavorite: slug =>
//     requests.del(`/articles/${slug}/favorite`),
//   update: article =>
//     requests.put(`/articles/${article.slug}`, {article: omitSlug(article)}),
//   create: article =>
//     requests.post('/articles', {article})
// };
//
// const Comments = {
//   create: (slug, comment) =>
//     requests.post(`/articles/${slug}/comments`, {comment}),
//   delete: (slug, commentId) =>
//     requests.del(`/articles/${slug}/comments/${commentId}`),
//   forArticle: slug =>
//     requests.get(`/articles/${slug}/comments`)
// };

const Profile = {
  follow: username =>
    requests.post(`/profiles/${username}/follow`),
  get: username =>
    requests.get(`/profiles/${username}`),
  unfollow: username =>
    requests.del(`/profiles/${username}/follow`)
};

// const Groups = {
//   create: group =>
//     requests.post('/groups', {groups}),
//   find: partialName =>
//     requests.get('/groups', partialName),
//   userGroups: user =>
//     requests.post('/groups', user),
// };

let groups = [
  {id: '1', name: 'My First Group'},
  {id: '2', name: 'Dumbledore\'s Army'},
  {id: '3', name: 'Death Devourers'},
  {id: '4', name: 'Клуб Веселых и Находчивых'}
];

const Groups = {
    userGroups: async user => {
        let answer = (await requests.get('/groups', user.id));
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
        return answer;
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
  create: news => {
    news.push(news);
    return {news: news[news.length - 1]}
  },
  forGroup: id => {
    return news.filter(news => news.groupId === id)
  }
};

let events = [
  {
    "id": 1,
    "title": "All Day Event",
    "start": "2018-02-02"
}];

const Events = {
  create: event => {
    return event;
  },
  del: id => {
    events = events.filter((event) => event.id !== id);
    return id;
  },
  edit: ({title, id, start, end}) => {
    console.log(events);
    const event = events.find(currentEvent => currentEvent.id === id);
    events = events.filter(currentEvent => currentEvent.id !== event.id);
    event.title = title;
    event.start = start;
    event.end = end;
    return event;
  },
    userEvents: async user => {
        let answer = (await requests.get('/events', user.id)).notes;
        console.log(">>>>>>>>>>>>>>>", answer);
        return answer;
    }
};

const Notes = {
  userNotes: async user => {
    let answer = (await requests.get('/notes', user.id)).notes;
    console.log(">>>>>>>>>>>>>>>", answer);
    return answer;
  },
  create: async note => {
    let answer = (await requests.post('/notes', {note}));
    console.log(">>>>>>>>>>>>>>>", answer);
    return answer;
  },

  del: async id => {
    let answer = (await requests.del(`/notes/${id}`));
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