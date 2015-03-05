'use strict';

angular.module('polcoApp')
    .factory('InterjectionTrans', function ($resource) {
        return $resource('api/interjectionTranss/:id', {}, {
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
