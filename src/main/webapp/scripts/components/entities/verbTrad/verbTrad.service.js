'use strict';

angular.module('polcoApp')
    .factory('VerbTrad', function ($resource) {
        return $resource('api/verbTrads/:id', {}, {
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
