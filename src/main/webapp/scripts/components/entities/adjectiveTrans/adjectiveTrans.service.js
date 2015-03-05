'use strict';

angular.module('polcoApp')
    .factory('AdjectiveTrans', function ($resource) {
        return $resource('api/adjectiveTranss/:id', {}, {
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
