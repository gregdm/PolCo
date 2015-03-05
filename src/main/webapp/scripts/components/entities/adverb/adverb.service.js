'use strict';

angular.module('polcoApp')
    .factory('Adverb', function ($resource) {
        return $resource('api/adverbs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
